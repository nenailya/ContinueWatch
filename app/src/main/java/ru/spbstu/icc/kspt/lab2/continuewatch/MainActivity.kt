package ru.spbstu.icc.kspt.lab2.continuewatch

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import ru.spbstu.icc.kspt.lab2.continuewatch.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

private lateinit var executor: ExecutorService

class MainActivity : AppCompatActivity() {
    private val LOG_TAG = "myLogs"
    private val SECONDS_ELAPSED = "Seconds elapsed"
    lateinit var textSecondsElapsed: TextView
    var secondsElapsed: Int = 0
    private var onTheScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        textSecondsElapsed = binding.textSecondsElapsed
        setContentView(binding.root)
        Log.d(LOG_TAG, "onCreate")
    }

    override fun onStart() {
        executor = Executors.newSingleThreadExecutor()
        executor.execute {
            while (!executor.isShutdown) {
                if (onTheScreen) {
                    Thread.sleep(1000)
                    Log.d(LOG_TAG, "${Thread.currentThread()}")
                    textSecondsElapsed.post {
                        textSecondsElapsed.text = "Seconds elapsed: " + secondsElapsed++
                    }
                }
            }
        }
        Log.d(LOG_TAG, "onStart")
        super.onStart()
    }

    override fun onPause() {
        executor.shutdown()
        onTheScreen = false
        super.onPause()
        Log.d(LOG_TAG, "onPause")
    }

    override fun onStop() {
        executor.shutdown()
        super.onStop()
        onTheScreen = false

        Log.d(LOG_TAG, "onStop")
    }
    override fun onResume() {
        super.onResume()
        onTheScreen = true
        Log.d(LOG_TAG, "onResume")
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