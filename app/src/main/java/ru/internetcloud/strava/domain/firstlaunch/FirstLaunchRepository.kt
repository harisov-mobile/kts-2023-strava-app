package ru.internetcloud.strava.domain.firstlaunch

interface FirstLaunchRepository {

    fun isFirstLaunch(): Boolean

    fun setFirstLaunchToFalse()
}
