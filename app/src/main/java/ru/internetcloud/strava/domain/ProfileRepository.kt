package ru.internetcloud.strava.domain

import ru.internetcloud.strava.domain.model.DataResponse
import ru.internetcloud.strava.domain.model.Profile

interface ProfileRepository {

    suspend fun getProfile(): DataResponse<Profile>
}
