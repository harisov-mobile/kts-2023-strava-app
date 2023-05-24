package ru.internetcloud.strava.data.onboarding

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import ru.internetcloud.strava.domain.onboarding.model.OnboardingParams

object OnboardingSharedPreferencesStorage {

    private const val ONBOARDING_SHARED_PREFS_NAME = "onboarding_shared_prefs"
    private const val KEY_FIRST_LAUNCH = "key_first_launch"

    private lateinit var onboardingSharedPrefs: SharedPreferences

    fun init(applicaton: Application) {
        onboardingSharedPrefs = applicaton.getSharedPreferences(ONBOARDING_SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getOnboardingParams(): OnboardingParams {
        return OnboardingParams(
            isFirstLaunch = onboardingSharedPrefs.getBoolean(KEY_FIRST_LAUNCH, false)
        )
    }

    fun saveOnboardingParams() {
        onboardingSharedPrefs.edit().putBoolean(KEY_FIRST_LAUNCH, true).apply()
    }
}
