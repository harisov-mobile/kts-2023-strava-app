package ru.internetcloud.strava.domain.onboarding.usecase

import ru.internetcloud.strava.domain.onboarding.OnboardingRepository

class SaveOnboardingParamsUseCase(private val onboardingRepository: OnboardingRepository) {

    fun saveOnboardingParams() {
        return onboardingRepository.saveOnboardingParams()
    }
}
