package ru.internetcloud.strava.domain.firstlaunch

interface FirstLaunchRepository {

    suspend fun isFirstLaunch(): Boolean

    suspend fun setFirstLaunchToFalse()
}
