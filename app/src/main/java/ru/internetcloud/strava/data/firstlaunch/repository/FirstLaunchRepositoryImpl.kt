package ru.internetcloud.strava.data.firstlaunch.repository

import ru.internetcloud.strava.data.firstlaunch.FirstLaunchSharedPreferencesStorage
import ru.internetcloud.strava.domain.firstlaunch.FirstLaunchRepository

class FirstLaunchRepositoryImpl(
    private val firstLaunchSharedPreferencesStorage: FirstLaunchSharedPreferencesStorage
) : FirstLaunchRepository {

    override suspend fun isFirstLaunch(): Boolean {
        return firstLaunchSharedPreferencesStorage.isFirstLaunch()
    }

    override suspend fun setFirstLaunchToFalse() {
        firstLaunchSharedPreferencesStorage.setFirstLaunchToFalse()
    }
}
