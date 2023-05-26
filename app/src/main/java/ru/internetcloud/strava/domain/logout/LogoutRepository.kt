package ru.internetcloud.strava.domain.logout

import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.logout.model.LogoutAnswer

interface LogoutRepository {

    suspend fun logout(): DataResponse<LogoutAnswer>
}
