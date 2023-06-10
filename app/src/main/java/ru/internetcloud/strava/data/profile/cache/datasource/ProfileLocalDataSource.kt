package ru.internetcloud.strava.data.profile.cache.datasource

import ru.internetcloud.strava.data.profile.cache.model.LocalProfile

interface ProfileLocalDataSource {

    suspend fun getProfile(): LocalProfile?

    suspend fun insertProfile(localProfile: LocalProfile)

    suspend fun deleteProfile()
}
