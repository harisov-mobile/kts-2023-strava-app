package ru.internetcloud.strava.presentation.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.internetcloud.strava.R
import ru.internetcloud.strava.databinding.FragmentOnBoardingBinding
import ru.internetcloud.strava.presentation.util.autoCleared

class OnBoardingFragment : Fragment(R.layout.fragment_on_boarding) {

    private val binding by viewBinding(FragmentOnBoardingBinding::bind)

    private var adapter: OnBoardingAdapter by autoCleared()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.getStartedButton.setOnClickListener {
            launchLoginFragment()
        }
    }

    private fun launchLoginFragment() {
        findNavController()
            .navigate(
                OnBoardingFragmentDirections.actionOnBoardingFragmentToAuthFragment(
                    messageResId = R.string.auth_standart_message
                )
            )
    }
}
