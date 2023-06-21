package ru.internetcloud.strava.domain.firstlaunch.usecase

import ru.internetcloud.strava.domain.firstlaunch.FirstLaunchRepository

class GetFirstLaunchUseCase(private val firstLaunchRepository: FirstLaunchRepository) {

    suspend fun isFirstLaunch(): Boolean {
        return firstLaunchRepository.isFirstLaunch()
    }
}
