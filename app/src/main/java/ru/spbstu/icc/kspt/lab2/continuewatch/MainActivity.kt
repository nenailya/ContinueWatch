package ru.spbstu.icc.kspt.lab2.continuewatch

import android.content.Context
import android.content.SharedPreferences
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
    private var onTheScreen = true
    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textSecondsElapsed = findViewById(R.id.textSecondsElapsed)
        sharedPref = getSharedPreferences(SECONDS_ELAPSED, Context.MODE_PRIVATE)
        lifecycleScope.launchWhenResumed {
            while (onTheScreen){
                delay(1000)
                Log.d(LOG_TAG, "coroutine")
                textSecondsElapsed.post {
                    textSecondsElapsed.text = "Seconds elapsed: " + secondsElapsed++
                }
            }
        }
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

}

