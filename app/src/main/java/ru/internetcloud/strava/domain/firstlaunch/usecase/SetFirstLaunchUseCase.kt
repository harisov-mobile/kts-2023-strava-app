package ru.internetcloud.strava.domain.firstlaunch.usecase

import ru.internetcloud.strava.domain.firstlaunch.FirstLaunchRepository

class SetFirstLaunchUseCase(private val firstLaunchRepository: FirstLaunchRepository) {

    fun setFirstLaunchToFalse() {
        return firstLaunchRepository.setFirstLaunchToFalse()
    }
}
