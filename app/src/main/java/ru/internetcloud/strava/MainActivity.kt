package ru.internetcloud.strava

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.internetcloud.strava.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        if (savedInstanceState == null) {
            showFragment(fragment = OnBoardingFragment(), withBackStack = false)
        }
    }

    private fun showFragment(fragment: Fragment, withBackStack: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()

        if (withBackStack) {
            transaction.replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        } else {
            transaction.add(R.id.fragment_container, fragment)
                .commit()
        }
    }
}
