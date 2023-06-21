package ru.internetcloud.strava.data.firstlaunch.repository

import ru.internetcloud.strava.data.firstlaunch.FirstLaunchSharedPreferencesStorage
import ru.internetcloud.strava.domain.firstlaunch.FirstLaunchRepository

class FirstLaunchRepositoryImpl : FirstLaunchRepository {

    override suspend fun isFirstLaunch(): Boolean {
        return FirstLaunchSharedPreferencesStorage.isFirstLaunch()
    }

    override suspend fun setFirstLaunchToFalse() {
        FirstLaunchSharedPreferencesStorage.setFirstLaunchToFalse()
    }
}
