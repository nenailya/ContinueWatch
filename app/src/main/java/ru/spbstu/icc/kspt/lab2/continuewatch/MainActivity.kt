package ru.spbstu.icc.kspt.lab2.continuewatch

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import ru.spbstu.icc.kspt.lab2.continuewatch.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

class MainActivity : AppCompatActivity() {
    private lateinit var future: Future<*>
    private val LOG_TAG = "myLogs"
    private val SECONDS_ELAPSED = "Seconds elapsed"
    private lateinit var textSecondsElapsed: TextView
    private var secondsElapsed: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        textSecondsElapsed = binding.textSecondsElapsed
        setContentView(binding.root)
    }

    override fun onStart() {
        future = background(App.executor)
        Log.d(LOG_TAG, "onStart")
        super.onStart()
    }

    override fun onStop() {
        future.cancel(true)
        super.onStop()
    }

    private fun background(executorService: ExecutorService) = executorService.submit {
        while (true) {
            Thread.sleep(1000)
            Log.d(LOG_TAG, "${Thread.currentThread()}")
            textSecondsElapsed.post {
                textSecondsElapsed.text = "${secondsElapsed++}"
            }
        }
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
            textSecondsElapsed.text = "$secondsElapsed"
        }
        Log.d(LOG_TAG, "onRestoreInstanceState")
    }
}