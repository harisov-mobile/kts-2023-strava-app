package ru.internetcloud.strava.domain.profile

import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.profile.model.Profile

interface ProfileRepository {

    suspend fun getProfile(): DataResponse<Profile>
}
