package ru.spbstu.icc.kspt.lab2.continuewatch

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

class MainActivity : AppCompatActivity() {
    val LOG_TAG = "myLogs"
    private val SECONDS_ELAPSED = "Seconds elapsed"
    var secondsElapsed: Int = 0
    lateinit var textSecondsElapsed: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textSecondsElapsed = findViewById(R.id.textSecondsElapsed)
        lifecycleScope.launchWhenResumed {
            while (isActive) {
                delay(1000)
                Log.d(LOG_TAG, SECONDS_ELAPSED + secondsElapsed)
                textSecondsElapsed.text = "${secondsElapsed++}"
            }
        }
        Log.d(LOG_TAG, "onCreate")
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

