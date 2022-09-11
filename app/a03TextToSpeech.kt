package org.shar35.audiobiblehk

import android.os.Bundle
import android.os.Environment
import android.speech.tts.TextToSpeech
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.util.*

/*

Text To Speech - Android Kotlin - Practical Step By Step Demo
https://www.youtube.com/watch?v=Iw_fwbnbz-w

<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".a03TextToSpeech">
    <Button
        android:id="@+id/button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:text="Button" />
</LinearLayout>

*/

class a03TextToSpeech : AppCompatActivity() {

    lateinit var button: Button
    lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a03_text_to_speech)

        button = findViewById(R.id.button)

        println( Locale.getDefault() )

        button.setOnClickListener {
            tts = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {
                if (it == TextToSpeech.SUCCESS) {
                    tts.setLanguage(Locale.TAIWAN)
                    tts.setSpeechRate(1.0f)
                    tts.speak("大家好", TextToSpeech.QUEUE_ADD, null)
                    println("大家好")
                    val textToConvert: String = "大家好"
                    //set up our arguments

                    // https://www.programcreek.com/java-api-examples/?class=android.speech.tts.TextToSpeech&method=synthesizeToFile

                    val utteranceId: String = "AuthorizationManager.createCodeVerifier()"
                    //set up our arguments
                    val params: HashMap<String, String> = HashMap()
                    params[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = utteranceId
                    //create a new unique ID
                    //   val utteranceId: String = AuthorizationManager.createCodeVerifier()
                    val fileLocation = "test.mp4"
                    val file = File(this.filesDir, fileLocation)
                    println(file.path)
                    tts.synthesizeToFile(textToConvert, params, file.path);
                }
            })


        }


    }
}