package ru.internetcloud.strava.domain.usecase

import ru.internetcloud.strava.domain.ProfileRepository
import ru.internetcloud.strava.domain.model.DataResponse
import ru.internetcloud.strava.domain.model.Profile

class GetProfileUseCase(private val profileRepository: ProfileRepository) {

    suspend fun getProfile(): DataResponse<Profile> {
        return profileRepository.getProfile()
    }
}
