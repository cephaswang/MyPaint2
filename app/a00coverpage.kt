package org.shar35.audiobiblehk

/*

2022-06-21

*/

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dd.plist.*
import org.xml.sax.SAXException
import java.io.IOException
import java.text.ParseException
import javax.xml.parsers.ParserConfigurationException


class a00coverpage : AppCompatActivity() {

    private val handler = Handler()
    private var MEDIA_PATH: String? = null
    // 資料庫參數
    private var helper: DBOpenHelper? = null
    private var db: SQLiteDatabase? = null
    private val dbVERSION = 1
    private val dbDBNAME = "b02config.db"
    private val REQ_PERMISSIONS = 0

    val GotoMenu =  Runnable() {
        val intent: Intent = Intent()
        intent.setClass(this, b01about::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a00coverpage)
        handler.postDelayed(GotoMenu, 4000)

        println("a00coverpage")

        MEDIA_PATH = filesDir.path

       // init_data()
        // init_bibleDB()
        askPermissions()
    }

    fun init_data(){
        // 資料庫初始化
        helper = DBOpenHelper(this@a00coverpage, dbDBNAME, null, dbVERSION)
        db = helper!!.getWritableDatabase()

        if (db != null){
            var b02SQL = ("CREATE TABLE IF NOT EXISTS BOOKMARK (WEB_ADDR TEXT PRIMARY KEY, FIELD_DATA1 TEXT,"
                        + "FIELD_DATA2 TEXT, FIELD_DATA3 TEXT, FIELD_DATA4 TEXT )")
            db!!.execSQL(b02SQL)
            println("建立資料表:$b02SQL")

            // 保存播放模式
            b02SQL = "INSERT OR REPLACE INTO FIELDS (FIELD_NAME, FIELD_DATA) VALUES ( 'SDK_INT' " +
                    ",  '" + Build.VERSION.SDK_INT.toString() + "' )"

            db!!.execSQL(b02SQL)
            db!!.close()
        }
    }

    fun init_bibleDB(){
        println("init_bibleDB")

        // All Android Directory Path
        // https://gist.github.com/lopspower/76421751b21594c69eb2
        /*
			一、把檔案聖經列表，覆製一份到工作目錄。
			二、三處用到，設定，搜尋，譯本選取。
			三、初始於第一次啟動時，完成基本佈署。
			    由設定，輸出給譯本選取。
			    基本設定，中文，英文。
			 */
        var rootDict = NSDictionary()
        var FontsDict = NSDictionary()

        val am = assets

        try {
            var `is` = am.open("bibledb/bibleLang_ini.plist")
            // 全部內容
            rootDict = PropertyListParser.parse(`is`) as NSDictionary
            `is`.close()

            // 字型檔
            `is` = am.open("bibledb/bible_Font_ini.plist")
            FontsDict = PropertyListParser.parse(`is`) as NSDictionary
            `is`.close()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ParserConfigurationException) {
            e.printStackTrace()
        } catch (e: SAXException) {
            e.printStackTrace()
        } catch (e: ParseException) {
            e.printStackTrace()
        } catch (e: PropertyListFormatException) {
            e.printStackTrace()
        }

        val names1ST   = rootDict.allKeys()
        val FontsArray = FontsDict.allKeys()

        val TaskALL = NSArray(names1ST.size + FontsArray.size - 1)

        var Qx = 0
        // 聖經文字資料表
        // 聖經文字資料表
        for (Jx in names1ST.indices) {

            // 註解用
            if (names1ST[Jx] == "000,None") {
                continue
            }
            val subAry = rootDict.objectForKey(names1ST[Jx]) as NSArray
            val BibeDB = names1ST[Jx].toString().substring(4)
            val TaskAry = NSArray(2)

            // 檔名
            TaskAry.setValue(0, NSString("$BibeDB.sqlite"))
            // 檔案容量
            TaskAry.setValue(1, NSString(subAry.objectAtIndex(4).toString()))
            TaskALL.setValue(Qx, TaskAry)
            Qx++
        }

        // 聖經文字資料表
        // 字型檔案

        // 聖經文字資料表
        // 字型檔案
        for (Jx in FontsArray.indices) {
            // 註解用
            if (FontsArray[Jx] == "0") {
                continue
            }
            val subAry = FontsDict.objectForKey(FontsArray[Jx]) as NSArray
            // 由打包內取出檔案。
            val TaskAry = NSArray(2)

            // 檔名
            TaskAry.setValue(0, NSString(subAry.objectAtIndex(1).toString()))
            // 容量
            TaskAry.setValue(1, NSString(subAry.objectAtIndex(2).toString()))
            TaskALL.setValue(Qx, TaskAry)
            Qx++
        }

        var TaskAry = NSArray(2)
        // 檔名
        // 檔名
        TaskAry.setValue(0, "bibleLang_ini.plist")
        // 容量
        // 容量
        TaskAry.setValue(1, "0")
        TaskALL.setValue(Qx, TaskAry)


        println("TaskALL.count():" + TaskALL.count())

        println("getFilesDir():$filesDir")
        try {
            for (Jx in 0 until TaskALL.count()) {
                TaskAry = TaskALL.objectAtIndex(Jx) as NSArray
                println("TaskAry.objectAtIndex(0):" + TaskAry.objectAtIndex(0))
                DCSTools().copyZipFile(
                    assets,
                    "bibledb/" + TaskAry.objectAtIndex(0),
                    MEDIA_PATH + "/bibledb/" + TaskAry.objectAtIndex(0)
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun askPermissions() {
        val PERMISSION_S = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.CHANGE_CONFIGURATION
        )
        val permissionsRequest: MutableSet<String> = HashSet()
        for (permission in PERMISSION_S) {
            val result = ContextCompat.checkSelfPermission(this, permission)
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionsRequest.add(permission)
            }
        }
        if (!permissionsRequest.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsRequest.toTypedArray(),
                REQ_PERMISSIONS
            )
        }
        println("permissionsRequest:" + permissionsRequest.size)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(GotoMenu)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        handler.removeCallbacks(GotoMenu)
        finish()
    }

}