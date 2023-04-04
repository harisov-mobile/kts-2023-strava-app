package ru.internetcloud.strava.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.internetcloud.strava.R
import timber.log.Timber

class TestActivity : AppCompatActivity(R.layout.activity_test) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate")
    }

    override fun onStart() {
        super.onStart()
        Timber.d("onStart")
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume")
    }

    override fun onPause() {
        super.onPause()
        Timber.d("onPause")
    }

    override fun onStop() {
        super.onStop()
        Timber.d("onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("onDestroy")
    }

    override fun onRestart() {
        super.onRestart()
        Timber.d("onRestart")
    }
}
