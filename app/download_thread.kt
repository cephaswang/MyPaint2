package org.shar35.audiobiblehk


import android.content.Context
import android.content.Intent
import android.os.StrictMode
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


// 安卓線程，檔案續傳，File is Curropted on resume Downloading in android Kotlin thread
// https://www.youtube.com/watch?v=b6NQ8QvdAwQ


class download_thread {

    val service: ExecutorService = Executors.newSingleThreadExecutor()
    val GET: String = "GET"
    var contexT: Context? = null

    // How can I fix 'android.os.NetworkOnMainThreadException'?
    // https://stackoverflow.com/questions/6343166/how-can-i-fix-android-os-networkonmainthreadexception
    val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()

    var params: Array<String>? = null
    var bufferIntent: Intent? = null
    var download_status_int: Int = 0
    var download_Mp3SaveName: String? = null

    public fun execute(
        context: Context,
        url: String,
        filePath: String,
        subDir: String,
        boadcast: String
    ) {
        params = arrayOf(url, filePath, subDir, boadcast)
        contexT = context
        StrictMode.setThreadPolicy(policy)
        bufferIntent = Intent(boadcast)

        var info: Boolean = false
        service.execute(Runnable {
            kotlin.run {
                try {
                    /*
                    val params: Array<String> = arrayOf(
                        "http://bible.cephas.tw/bible_cantonese/004/4_001.mp3",
                        context.filesDir.path + "/audio"
                    )*/
                    info = requestGET_file(params!!)
                } catch (e: Exception) {
                    println(e.toString())
                } finally {
                    if (info) {
                        download_status_int = 101
                        println("pass")
                    } else {
                        download_status_int = 102
                        println("fail")
                    }
                    sendBufferingBroadcast()
                }
            }
        })
    }

    private fun sendBufferingBroadcast() {
        bufferIntent!!.putExtra("buffering", "1")
        bufferIntent!!.putExtra("StatusValue", String.format("%d", download_status_int))
        // 保存檔名
        bufferIntent!!.putExtra("Mp3SaveName", download_Mp3SaveName)
        contexT?.sendBroadcast(bufferIntent);
    }

    @Throws(IOException::class)
    fun requestGET_file(params: Array<String>): Boolean {
        val urlFILE = params[0] // 下載的文檔網址
        val sotrePATH = params[1] // 保存的路徑
        val subDir = params[2] // 子目錄

        val fileArray = urlFILE.split("/").toTypedArray()
        // 保存的檔名
        val fileSTORE = fileArray[fileArray.size - 1]

        download_Mp3SaveName = fileSTORE
        var pathSUB: String? = ""
        var pathALL: String? = ""

        if (subDir.length > 0) {
            // "abc/dir/aux"
            val path_array = subDir.split("/").toTypedArray()
            for (Ix in 0..path_array.size - 1 step 1) {
                pathSUB += "/" + path_array[Ix]
                pathALL = sotrePATH + pathSUB
                try {
                    Files.createDirectory(Paths.get(pathALL))
                    println(" createDirectory: " + pathALL)
                } catch (e: IOException) {
                    println(e.localizedMessage.toString())
                    return false
                }
            }
        } else {
            pathALL = sotrePATH
        }

        var exfile = File(pathALL, fileSTORE)
        if (exfile.exists()) {
            return true
        }

        // 未下載，斷點續傳
        var sotreSTAT: Long = 0
        exfile = File(pathALL, "$fileSTORE.down")
        if (exfile.exists()) {
            sotreSTAT = exfile.length()
        }

        val obj = URL(urlFILE)
        var urlConnection = obj.openConnection() as HttpURLConnection

        // 檔案容量長度
        val lenghtOfFile: Long = urlConnection.contentLength.toLong()
        urlConnection.disconnect()

        // 巳完成下載
        if (sotreSTAT == lenghtOfFile) {
            val fromFile = File(pathALL, "$fileSTORE.down")
            val destFile = File(pathALL, fileSTORE)
            fromFile.renameTo(destFile)
            return true
        }

        // 異常，大於原有檔案
        if (sotreSTAT > lenghtOfFile) {
            exfile.delete()
            sotreSTAT = 0
        }

        urlConnection = obj.openConnection() as HttpURLConnection

        // 續傳檔案
        if (sotreSTAT > 0 && sotreSTAT < lenghtOfFile) {
            urlConnection.setRequestProperty(
                "Range", String.format("bytes=%d-", sotreSTAT)
            )
        }

        urlConnection.requestMethod = GET
        val responseCode = urlConnection.responseCode
        println("Response Code :: $responseCode")

        return if (responseCode == HttpURLConnection.HTTP_OK) { // connection ok
            try {
                // 沒有保存路徑，則保存到手機內部記憶體
                var outStream: RandomAccessFile? = null
                outStream = RandomAccessFile(exfile, "rw")
                outStream.seek(sotreSTAT)

                val inputStream: InputStream = BufferedInputStream(urlConnection.inputStream)
                val bytes = ByteArray(1024)
                var read: Int = 0

                var size: Long = sotreSTAT
                while (inputStream.read(bytes).also { read = it } != -1) {
                    outStream.write(bytes, 0, read)
                    size += read
                    download_status_int = (size * 100 / lenghtOfFile).toInt()
                    sendBufferingBroadcast()
                }
                inputStream.close()
                outStream.close()

                val fromFile = File(pathALL, "$fileSTORE.down")
                val destFile = File(pathALL, fileSTORE)
                fromFile.renameTo(destFile)

                println(" destFile: " + destFile)
                println("pass size: " + size)
            } catch (e: Exception) {
                return false
            }
            return true
        } else {
            return false
        }
    }


}