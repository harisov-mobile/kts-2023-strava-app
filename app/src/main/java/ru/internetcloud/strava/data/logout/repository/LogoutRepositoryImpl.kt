package ru.internetcloud.strava.data.logout.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.internetcloud.strava.data.common.ErrorMessageConverter
import ru.internetcloud.strava.data.common.StravaApiFactory
import ru.internetcloud.strava.data.logout.mapper.LogoutMapper
import ru.internetcloud.strava.data.token.TokenSharedPreferencesStorage
import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.common.model.Source
import ru.internetcloud.strava.domain.logout.LogoutRepository
import ru.internetcloud.strava.domain.logout.model.LogoutAnswer

class LogoutRepositoryImpl : LogoutRepository {

    private val logoutMapper = LogoutMapper()

    override suspend fun logout(): DataResponse<LogoutAnswer> {
        return try {
            val networkResponse = StravaApiFactory.logoutApi.logout()
            withContext(Dispatchers.IO) {
                clearToken() // в любом случае надо удалить в SharedPrefs информацию о токене!
            }
            if (networkResponse.isSuccessful) {
                val logoutAnswerDTO = networkResponse.body()
                logoutAnswerDTO?.let { currentDTO ->
                    DataResponse.Success(logoutMapper.fromDtoToDomain(currentDTO), source = Source.RemoteApi)
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

    private suspend fun clearToken() {
        TokenSharedPreferencesStorage.clearTokenData()
    }
}
