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
    lateinit var backgroundThread: Thread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textSecondsElapsed = findViewById(R.id.textSecondsElapsed)
        textSecondsElapsed.text = "Seconds elapsed: " + secondsElapsed
        Log.d(LOG_TAG, "onCreate")
    }

    override fun onStop() {
        backgroundThread.interrupt()
        super.onStop()
        onTheScreen = false
        Log.d(LOG_TAG, "onStop")
    }

    override fun onStart() {
        onTheScreen = true
        backgroundThread = Thread {
            try {
                while (!Thread.currentThread().isInterrupted) {
                    Thread.sleep(1000)
                    Log.d(LOG_TAG, "${Thread.currentThread()}")
                    if (onTheScreen) {
                        textSecondsElapsed.post {
                            textSecondsElapsed.text = "Seconds elapsed: " + secondsElapsed++
                        }
                    }
                }
            } catch (ex: InterruptedException) {
            }
        }
        backgroundThread.start()
        super.onStart()
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

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putInt(SECONDS_ELAPSED, secondsElapsed)
        }
        Log.d(LOG_TAG, "onSaveInstanceState")
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.run {
            secondsElapsed = getInt(SECONDS_ELAPSED)
            textSecondsElapsed.text = "Seconds elapsed: " + secondsElapsed
        }
        Log.d(LOG_TAG, "onRestoreInstanceState")
    }
}

