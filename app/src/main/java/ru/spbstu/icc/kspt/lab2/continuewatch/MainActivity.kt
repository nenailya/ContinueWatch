package ru.spbstu.icc.kspt.lab2.continuewatch

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import ru.spbstu.icc.kspt.lab2.continuewatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val LOG_TAG = "myLogs"
    private val SECONDS_ELAPSED = "Seconds elapsed"
    var secondsElapsed: Long = 0
    var time: Long = 0
    var time2: Long = 0
    lateinit var textSecondsElapsed: TextView
    private var onTheScreen = true
    lateinit var backgroundThread: Thread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        textSecondsElapsed = binding.textSecondsElapsed
        setContentView(binding.root)
        Log.d(LOG_TAG, "onCreate")
    }

    override fun onStart() {
        backgroundThread = Thread {
            try {
                while (!Thread.currentThread().isInterrupted) {
                    time = System.currentTimeMillis()
                    Thread.sleep(1000)
                    Log.d(LOG_TAG, "${Thread.currentThread()}")
                    if (onTheScreen) {
                        time2 = System.currentTimeMillis()
                        secondsElapsed += time2 - time
                        textSecondsElapsed.post {
                            textSecondsElapsed.text = "${secondsElapsed/1000}"
                        }
                    }
                }
            } catch (ex: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }
        onTheScreen = true
        backgroundThread.start()
        super.onStart()
        Log.d(LOG_TAG, "onStart")
    }

    override fun onStop() {
        backgroundThread.interrupt()
        super.onStop()
        onTheScreen = false
        Log.d(LOG_TAG, "onStop")
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putLong(SECONDS_ELAPSED, secondsElapsed)
        }
        Log.d(LOG_TAG, "onSaveInstanceState")
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.run {
            secondsElapsed = getLong(SECONDS_ELAPSED)
            textSecondsElapsed.text = "$secondsElapsed"
        }
        Log.d(LOG_TAG, "onRestoreInstanceState")
    }
}


