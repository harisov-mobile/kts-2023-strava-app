package ru.internetcloud.strava.data.logout.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.internetcloud.strava.data.common.ErrorMessageConverter
import ru.internetcloud.strava.data.logout.mapper.LogoutMapper
import ru.internetcloud.strava.data.logout.network.api.LogoutApi
import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.common.model.Source
import ru.internetcloud.strava.domain.logout.LogoutRepository
import ru.internetcloud.strava.domain.logout.model.LogoutAnswer
import ru.internetcloud.strava.domain.token.TokenRepository

class LogoutRepositoryImpl(
    private val logoutApi: LogoutApi,
    private val logoutMapper: LogoutMapper,
    private val tokenRepository: TokenRepository,
    private val errorMessageConverter: ErrorMessageConverter
) : LogoutRepository {

    override suspend fun logout(): DataResponse<LogoutAnswer> {
        return try {
            val networkResponse = logoutApi.logout()
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
                DataResponse.Error(Exception(errorMessageConverter.getMessageToHTTPCode(networkResponse.code())))
            }
        } catch (e: Exception) {
            DataResponse.Error(Exception(errorMessageConverter.getMessageToException(e)))
        }
    }

    private suspend fun clearToken() {
        tokenRepository.clearTokenData()
    }
}
