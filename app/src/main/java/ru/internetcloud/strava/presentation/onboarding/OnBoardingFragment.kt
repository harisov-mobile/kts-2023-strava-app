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

    private fun initAdapter() {
        adapter = OnBoardingAdapter(
            listOf(
                OnBoardingUI(
                    imageResId = R.drawable.strava_bikers,
                    titleResId = R.string.onboarding_slogan_bikers
                ),
                OnBoardingUI(
                    imageResId = R.drawable.strava_runner,
                    titleResId = R.string.onboarding_slogan_runner
                ),
                OnBoardingUI(
                    imageResId = R.drawable.strava_pool,
                    titleResId = R.string.onboarding_slogan_pool
                ),
                OnBoardingUI(
                    imageResId = R.drawable.strava_people,
                    titleResId = R.string.onboarding_slogan_people
                )
            )
        )

        with(binding) {
            onBoardingVewPager.adapter = adapter
            dotsIndicator.setViewPager2(onBoardingVewPager)
        }
    }
}
