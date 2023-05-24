package ru.internetcloud.strava.data.onboarding.repository

import ru.internetcloud.strava.data.onboarding.OnboardingSharedPreferencesStorage
import ru.internetcloud.strava.domain.onboarding.OnboardingRepository
import ru.internetcloud.strava.domain.onboarding.model.OnboardingParams

class OnboardingRepositoryImpl : OnboardingRepository {

    override fun getOnboardingParams(): OnboardingParams {
        return OnboardingSharedPreferencesStorage.getOnboardingParams()
    }

    override fun saveOnboardingParams() {
        OnboardingSharedPreferencesStorage.saveOnboardingParams()
    }
}
