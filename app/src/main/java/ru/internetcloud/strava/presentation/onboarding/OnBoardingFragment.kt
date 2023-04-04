package ru.internetcloud.strava.presentation.onboarding

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.color.MaterialColors
import ru.internetcloud.strava.R
import ru.internetcloud.strava.databinding.FragmentOnBoardingBinding
import timber.log.Timber

class OnBoardingFragment : Fragment(R.layout.fragment_on_boarding) {

    private val binding by viewBinding(FragmentOnBoardingBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideBars()

        binding.getStartedButton.setOnClickListener {
            launchLoginFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        showBars()
    }

    private fun launchLoginFragment() {
        Timber.tag("rustam").d("launchLoginFragment")
        findNavController().navigate(R.id.action_onBoardingFragment_to_loginFragment)
    }

    private fun hideBars() {
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) // hide status bar
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }

    private fun showBars() {
        val colorPrimaryVariant =
            MaterialColors.getColor(
                requireContext(),
                com.google.android.material.R.attr.colorPrimaryVariant,
                "no colorPrimaryVariant definition"
            )

        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        requireActivity().window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            )
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        requireActivity().window.statusBarColor = colorPrimaryVariant

        (requireActivity() as AppCompatActivity).supportActionBar?.show()
    }
}
