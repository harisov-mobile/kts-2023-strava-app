package ru.internetcloud.strava.domain.profile.usecase

import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.profile.ProfileRepository
import ru.internetcloud.strava.domain.profile.model.Profile

class GetProfileUseCase(private val profileRepository: ProfileRepository) {

    suspend fun getProfile(): DataResponse<Profile> {
        return profileRepository.getProfile()
    }
}
