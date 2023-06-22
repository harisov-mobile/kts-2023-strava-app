package ru.internetcloud.strava.domain.profile.usecase

import ru.internetcloud.strava.domain.profile.ProfileRepository

class DeleteProfileInLocalCacheUseCase(private val profileRepository: ProfileRepository) {

    suspend fun deleteProfileInLocalCache() {
        profileRepository.deleteProfileInLocalCache()
    }
}
