package ru.spbstu.icc.kspt.lab2.continuewatch

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.util.Log

class MainActivity_2 : AppCompatActivity() {
    val LOG_TAG = "myLogs"
    private val SECONDS_ELAPSED = "Seconds elapsed"
    var secondsElapsed: Int = 0
    lateinit var textSecondsElapsed: TextView
    private var onTheScreen = true
    lateinit var sharedPref: SharedPreferences

    var backgroundThread = Thread {
        try {
            while (true) {
                Thread.sleep(1000)
                if (onTheScreen) {
                    textSecondsElapsed.post {
                        textSecondsElapsed.setText("Seconds elapsed: " + secondsElapsed++)
                    }
                }
            }
        } catch (ex: InterruptedException) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPref = getSharedPreferences(
            "Seconds elapsed: " + secondsElapsed++, Context.MODE_PRIVATE)
        textSecondsElapsed = findViewById(R.id.textSecondsElapsed)
        secondsElapsed = sharedPref.getInt(SECONDS_ELAPSED, 0)
        textSecondsElapsed.setText("Seconds elapsed: " + secondsElapsed)
        backgroundThread.start()
        Log.d(LOG_TAG, "onCreate")
    }

    override fun onStop() {
        super.onStop()
        onTheScreen = false
        with (sharedPref.edit()) {
            putInt(SECONDS_ELAPSED, secondsElapsed)
            apply()
        }
        Log.d(LOG_TAG, "onStop")
    }

    override fun onStart() {
        super.onStart()
        onTheScreen = true
        secondsElapsed = sharedPref.getInt(SECONDS_ELAPSED, 0)
        Log.d(LOG_TAG, "onStart")
    }

    override fun onPause() {
        super.onPause()
        onTheScreen = false
        Log.d(LOG_TAG, "onPause")
    }

    override fun onResume() {
        super.onResume()
        onTheScreen = true
        Log.d(LOG_TAG, "onResume")
    }

    override fun onDestroy() {
        super.onDestroy()
        backgroundThread.interrupt()
        Log.d(LOG_TAG, "onDestroy")
    }
}