package ru.spbstu.icc.kspt.lab2.continuewatch

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import ru.spbstu.icc.kspt.lab2.continuewatch.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    val executor: ExecutorService = Executors.newSingleThreadExecutor()
    private val LOG_TAG = "myLogs"
    private val SECONDS_ELAPSED = "Seconds elapsed"
    var secondsElapsed: Int = 0
    lateinit var textSecondsElapsed: TextView
    private var onTheScreen = true
    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        textSecondsElapsed.text = "Seconds elapsed: $secondsElapsed"
        Log.d(LOG_TAG, "onCreate")
        setContentView(binding.root)
    }

    override fun onStop() {
        executor.shutdown()
        super.onStop()
        Log.d(LOG_TAG, "onStop")
    }

    override fun onStart() {
        executor.submit {
            while (!executor.isShutdown) {
                Log.d(LOG_TAG, "${Thread.currentThread()} is iterating")
                Thread.sleep(1000)
                textSecondsElapsed.post {
                    textSecondsElapsed.text = "${secondsElapsed++}"
                }
            }
        }
        super.onStart()
        onTheScreen = true
        secondsElapsed = sharedPref.getInt(SECONDS_ELAPSED, 0)
        Log.d(LOG_TAG, "onStart")
    }

    override fun onPause() {
        executor.shutdown()
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
        Log.d(LOG_TAG, "onDestroy")
    }
}