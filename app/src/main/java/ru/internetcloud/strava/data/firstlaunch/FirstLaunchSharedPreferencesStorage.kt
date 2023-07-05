package ru.internetcloud.strava.data.firstlaunch

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class FirstLaunchSharedPreferencesStorage(
    applicaton: Application
) {
    private var firstLaunchSharedPrefs: SharedPreferences =
        applicaton.getSharedPreferences(FIRST_LAUNCH_SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    suspend fun isFirstLaunch(): Boolean = firstLaunchSharedPrefs.getBoolean(KEY_FIRST_LAUNCH, true)

    suspend fun setFirstLaunchToFalse() {
        firstLaunchSharedPrefs.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply()
    }

    companion object {
        private const val FIRST_LAUNCH_SHARED_PREFS_NAME = "first_launch_shared_prefs"
        private const val KEY_FIRST_LAUNCH = "key_first_launch"
    }
}
