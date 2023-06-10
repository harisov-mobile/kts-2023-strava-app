package ru.internetcloud.strava.data.firstlaunch.repository

import ru.internetcloud.strava.data.firstlaunch.FirstLaunchSharedPreferencesStorage
import ru.internetcloud.strava.domain.firstlaunch.FirstLaunchRepository

class FirstLaunchRepositoryImpl : FirstLaunchRepository {

    override fun isFirstLaunch(): Boolean {
        return FirstLaunchSharedPreferencesStorage.isFirstLaunch()
    }

    override fun setFirstLaunchToFalse() {
        FirstLaunchSharedPreferencesStorage.setFirstLaunchToFalse()
    }
}
