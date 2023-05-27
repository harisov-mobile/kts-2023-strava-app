package ru.internetcloud.strava.data.logout.repository

import ru.internetcloud.strava.data.auth.network.TokenStorage
import ru.internetcloud.strava.data.common.ErrorMessageConverter
import ru.internetcloud.strava.data.common.StravaApiFactory
import ru.internetcloud.strava.data.logout.mapper.LogoutMapper
import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.logout.LogoutRepository
import ru.internetcloud.strava.domain.logout.model.LogoutAnswer

class LogoutRepositoryImpl : LogoutRepository {

    private val logoutMapper = LogoutMapper()

    override suspend fun logout(): DataResponse<LogoutAnswer> {
        return try {
            val networkResponse = StravaApiFactory.logoutApi.logout()
            clearToken() // в любом случае надо удалить в SharedPrefs информацию о токене!
            if (networkResponse.isSuccessful) {
                val logoutAnswerDTO = networkResponse.body()
                logoutAnswerDTO?.let { currentDTO ->
                    DataResponse.Success(logoutMapper.fromDtoToDomain(currentDTO))
                } ?: let {
                    DataResponse.Error(exception = IllegalStateException("No logout answer found"))
                }
            } else {
                DataResponse.Error(Exception(ErrorMessageConverter.getMessageToHTTPCode(networkResponse.code())))
            }
        } catch (e: Exception) {
            DataResponse.Error(Exception(ErrorMessageConverter.getMessageToException(e)))
        }
    }

    private fun clearToken() {
        TokenStorage.accessToken = null
        TokenStorage.refreshToken = null
        TokenStorage.idToken = null

        TokenStorage.saveTokenToPrefs()
    }
}
