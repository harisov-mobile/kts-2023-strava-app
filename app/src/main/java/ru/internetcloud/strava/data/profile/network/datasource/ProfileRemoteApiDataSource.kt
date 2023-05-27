package ru.internetcloud.strava.data.profile.network.datasource

import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.profile.model.Profile

interface ProfileRemoteApiDataSource {

    suspend fun getProfile(): DataResponse<Profile>
}
