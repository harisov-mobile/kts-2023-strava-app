package ru.internetcloud.strava.presentation.start

import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import ru.internetcloud.strava.data.onboarding.repository.OnboardingRepositoryImpl
import ru.internetcloud.strava.domain.onboarding.usecase.GetOnboardingParamsUseCase
import ru.internetcloud.strava.domain.onboarding.usecase.SaveOnboardingParamsUseCase

class StartViewModel : ViewModel() {

    private val onboardingRepository = OnboardingRepositoryImpl()
    private val getOnboardingParamsUseCase = GetOnboardingParamsUseCase(onboardingRepository)
    private val saveOnboardingParamsUseCase = SaveOnboardingParamsUseCase(onboardingRepository)

    fun getDirectionToNavigate(): NavDirections {
        val onboardingParams = getOnboardingParamsUseCase.getOnboardingParams()

        return if (onboardingParams.isFirstLaunch) {
            saveOnboardingParamsUseCase.saveOnboardingParams()
            StartFragmentDirections.actionStartFragmentToOnBoardingFragment()
        } else {
            StartFragmentDirections.actionStartFragmentToAuthFragment()
        }
    }
}
