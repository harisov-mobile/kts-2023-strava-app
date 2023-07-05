package ru.internetcloud.strava.data.profile.network.datasource

import ru.internetcloud.strava.data.common.ErrorMessageConverter
import ru.internetcloud.strava.data.profile.mapper.ProfileMapper
import ru.internetcloud.strava.data.profile.network.api.ProfileApi
import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.common.model.Source
import ru.internetcloud.strava.domain.profile.model.Profile

class ProfileRemoteApiDataSourceImpl(
    private val profileApi: ProfileApi,
    private val profileMapper: ProfileMapper,
    private val errorMessageConverter: ErrorMessageConverter
) : ProfileRemoteApiDataSource {

    override suspend fun getProfile(): DataResponse<Profile> {
        return try {
            val networkResponse = profileApi.getProfile()
            if (networkResponse.isSuccessful) {
                val stravaAthleteDTO = networkResponse.body()
                stravaAthleteDTO?.let { currentDTO ->
                    val profile = profileMapper.fromDtoToDomain(currentDTO)
                    DataResponse.Success(profile, source = Source.RemoteApi)
                } ?: let {
                    DataResponse.Error(exception = IllegalStateException("No profile found"))
                }
            } else {
                DataResponse.Error(Exception(errorMessageConverter.getMessageToHTTPCode(networkResponse.code())))
            }
        } catch (e: Exception) {
            DataResponse.Error(Exception(errorMessageConverter.getMessageToException(e)))
        }
    }
}
