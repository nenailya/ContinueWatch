package ru.spbstu.icc.kspt.lab2.continuewatch

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
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
        setContentView(R.layout.activity_main)
        textSecondsElapsed.text = "Seconds elapsed: $secondsElapsed"
        sharedPref = getSharedPreferences(SECONDS_ELAPSED, Context.MODE_PRIVATE)

        lifecycleScope.launchWhenResumed {
            while (true){
                delay(1000)
                Log.d(LOG_TAG, "coroutine")
                textSecondsElapsed.post {
                    textSecondsElapsed.text = "${secondsElapsed++}"
            }
            }
        }

        Log.d(LOG_TAG, "onCreate")
    }

    override fun onStop() {
        executor.shutdown()
        super.onStop()
        Log.d(LOG_TAG, "onStop")
    }

    override fun onStart() {
        onTheScreen = true
        secondsElapsed = sharedPref.getInt(SECONDS_ELAPSED, 0)
        Log.d(LOG_TAG, "onStart")
        super.onStart()
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
        Log.d(LOG_TAG, "onDestroy")
    }
}