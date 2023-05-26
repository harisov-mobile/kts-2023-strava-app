package ru.internetcloud.strava.domain.logout.usecase

import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.logout.LogoutRepository
import ru.internetcloud.strava.domain.logout.model.LogoutAnswer

class LogoutUseCase(private val logoutRepository: LogoutRepository) {

    suspend fun logout(): DataResponse<LogoutAnswer> {
        return logoutRepository.logout()
    }
}
