package ru.internetcloud.strava.domain.onboarding

import ru.internetcloud.strava.domain.onboarding.model.OnboardingParams

interface OnboardingRepository {

    fun getOnboardingParams(): OnboardingParams

    fun saveOnboardingParams()
}
