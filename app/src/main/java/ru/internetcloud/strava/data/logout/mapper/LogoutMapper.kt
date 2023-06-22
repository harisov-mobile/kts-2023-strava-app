package ru.internetcloud.strava.data.logout.mapper

import ru.internetcloud.strava.data.logout.network.model.LogoutAnswerDTO
import ru.internetcloud.strava.domain.logout.model.LogoutAnswer

class LogoutMapper {

    fun fromDtoToDomain(logoutAnswerDTO: LogoutAnswerDTO): LogoutAnswer {
        return LogoutAnswer(
            accessToken = logoutAnswerDTO.accessToken
        )
    }
}
