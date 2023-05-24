package ru.internetcloud.strava.domain.onboarding.usecase

import ru.internetcloud.strava.domain.onboarding.OnboardingRepository
import ru.internetcloud.strava.domain.onboarding.model.OnboardingParams

class GetOnboardingParamsUseCase(private val onboardingRepository: OnboardingRepository) {

    fun getOnboardingParams(): OnboardingParams {
        return onboardingRepository.getOnboardingParams()
    }
}
