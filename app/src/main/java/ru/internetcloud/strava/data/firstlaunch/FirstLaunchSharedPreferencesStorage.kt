package ru.internetcloud.strava.data.firstlaunch

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

object FirstLaunchSharedPreferencesStorage {

    private const val FIRST_LAUNCH_SHARED_PREFS_NAME = "first_launch_shared_prefs"
    private const val KEY_FIRST_LAUNCH = "key_first_launch"

    private lateinit var firstLaunchSharedPrefs: SharedPreferences

    fun init(applicaton: Application) {
        firstLaunchSharedPrefs = applicaton.getSharedPreferences(FIRST_LAUNCH_SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun isFirstLaunch(): Boolean = firstLaunchSharedPrefs.getBoolean(KEY_FIRST_LAUNCH, true)

    fun setFirstLaunchToFalse() {
        firstLaunchSharedPrefs.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply()
    }
}
