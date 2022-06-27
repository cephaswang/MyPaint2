package org.shar35.audiobiblehk

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class b01about : AppCompatActivity() {

    val dn: download_thread = download_thread()
    lateinit var button: Button
    lateinit var textView: TextView
    lateinit var progressBar: ProgressBar

    // Progress dialogue and broadcast receiver variables
    var mBufferBroadcastIsRegistered = false
    val b01about_boadcast: String = "b01about_boadcast"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b01about)

        textView = findViewById(R.id.textView)
        button = findViewById(R.id.button)
        progressBar = findViewById(R.id.progressBar)

        // Register broadcast receiver
        if (!mBufferBroadcastIsRegistered) {
            registerReceiver(
                broadcastBufferReceiver, IntentFilter(
                    b01about_boadcast
                )
            )
            mBufferBroadcastIsRegistered = true
        }

        button.setOnClickListener {
            dn.execute(
                this,
                "http://bible.cephas.tw/bible_cantonese/004/4_001.mp3",
                this.filesDir.path,
                "abc/dir/aux",
                b01about_boadcast
            );
/*
"audio/004",
"",
"abc/dir/aux",
*/

        }

    }

    // Set up broadcast receiver
    private val broadcastBufferReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, bufferIntent: Intent) {
            val bufferValue = bufferIntent.getStringExtra("buffering")
// 傳入進度睥值
            val StatusValue = bufferIntent.getStringExtra("StatusValue")
            var Mp3SaveName = bufferIntent.getStringExtra("Mp3SaveName")
            textView.setText(bufferValue + "," + Mp3SaveName + "," + StatusValue)

            if (StatusValue != null) {
                if (StatusValue.toInt() < 100) {
                    progressBar.progress = StatusValue.toInt()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
// Unregister broadcast receiver
        if (mBufferBroadcastIsRegistered) {
            unregisterReceiver(broadcastBufferReceiver)
            mBufferBroadcastIsRegistered = false
        }
    }
}