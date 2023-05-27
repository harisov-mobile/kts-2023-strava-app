package ru.internetcloud.strava.data.profile.cache.datasource

import ru.internetcloud.strava.domain.profile.model.Profile

interface ProfileLocalDataSource {

    suspend fun getProfile(): Profile
}
