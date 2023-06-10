package ru.internetcloud.strava.domain.firstlaunch.usecase

import ru.internetcloud.strava.domain.firstlaunch.FirstLaunchRepository

class GetFirstLaunchUseCase(private val firstLaunchRepository: FirstLaunchRepository) {

    fun isFirstLaunch(): Boolean {
        return firstLaunchRepository.isFirstLaunch()
    }
}
