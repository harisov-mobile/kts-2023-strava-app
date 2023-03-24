package ru.internetcloud.strava

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.color.MaterialColors
import ru.internetcloud.strava.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnBoardingFragment.OnBoardingEvents {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var onBoardingFragment: OnBoardingFragment? = null

    companion object {
        const val FRAGMENT_ONBOARDING_TAG = "on_boarding_fragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            hideBars()
            showFragment(fragment = OnBoardingFragment(), withBackStack = false, tag = FRAGMENT_ONBOARDING_TAG)
        } else {
            onBoardingFragment =
                supportFragmentManager.findFragmentByTag(FRAGMENT_ONBOARDING_TAG) as? OnBoardingFragment
            if (onBoardingFragment == null) {
                showBars()
            } else {
                hideBars()
            }
        }
    }

    private fun showFragment(fragment: Fragment, withBackStack: Boolean, tag: String? = null) {
        val transaction = supportFragmentManager.beginTransaction()

        if (withBackStack) {
            transaction.addToBackStack(null)
        }

        transaction.replace(R.id.fragment_container, fragment, tag)
            .commit()
    }

    override fun onShowSecondFragment() {
        showBars()
        val fragment = SecondFragment()
        showFragment(fragment = fragment, withBackStack = false)
    }

    private fun hideBars() {
        getSupportActionBar()?.hide() // hide the title bar
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) // hide status bar
    }

    private fun showBars() {
        val colorPrimaryVariant =
            MaterialColors.getColor(
                this,
                com.google.android.material.R.attr.colorPrimaryVariant,
                "no colorPrimaryVariant definition"
            )

        val window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            )
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = colorPrimaryVariant

        getSupportActionBar()?.show() // show the title bar
    }
}
