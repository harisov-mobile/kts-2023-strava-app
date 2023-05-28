package ru.internetcloud.strava.data.profile.cache.datasource

import ru.internetcloud.strava.data.profile.cache.model.ProfileDbModel

interface ProfileLocalDataSource {

    suspend fun getProfile(): ProfileDbModel?

    suspend fun insertProfile(profileDbModel: ProfileDbModel)

    suspend fun deleteProfile()
}
