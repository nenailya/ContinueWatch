package ru.spbstu.icc.kspt.lab2.continuewatch

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.util.Log

class MainActivity : AppCompatActivity() {
    val LOG_TAG = "myLogs"
    private val SECONDS_ELAPSED = "Seconds elapsed"
    var secondsElapsed: Int = 0
    lateinit var textSecondsElapsed: TextView
    private var onTheScreen = true

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
        textSecondsElapsed = findViewById(R.id.textSecondsElapsed)
        textSecondsElapsed.setText("Seconds elapsed: " + secondsElapsed)
        backgroundThread.start()
        Log.d(LOG_TAG, "onCreate")
    }

    override fun onStop() {
        super.onStop()
        onTheScreen = false
        Log.d(LOG_TAG, "onStop")
    }

    override fun onStart() {
        super.onStart()
        onTheScreen = true
        Log.d(LOG_TAG, "onStart")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putInt(SECONDS_ELAPSED,secondsElapsed)
        }
        Log.d(LOG_TAG, "onSaveInstanceState")
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.run {
            secondsElapsed = getInt(SECONDS_ELAPSED)
            textSecondsElapsed.setText("Seconds elapsed: " + secondsElapsed)
        }
        Log.d(LOG_TAG, "onRestoreInstanceState")
    }
}

