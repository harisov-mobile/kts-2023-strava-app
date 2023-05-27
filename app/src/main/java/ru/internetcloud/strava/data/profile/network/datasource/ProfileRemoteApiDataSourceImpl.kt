package ru.internetcloud.strava.data.profile.network.datasource

import ru.internetcloud.strava.data.common.ErrorMessageConverter
import ru.internetcloud.strava.data.common.StravaApiFactory
import ru.internetcloud.strava.data.profile.mapper.ProfileMapper
import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.profile.model.Profile

class ProfileRemoteApiDataSourceImpl : ProfileRemoteApiDataSource {

    private val profileMapper = ProfileMapper()
    private val profileApi = StravaApiFactory.profileApi

    override suspend fun getProfile(): DataResponse<Profile> {
        return try {
            val networkResponse = profileApi.getProfile()
            if (networkResponse.isSuccessful) {
                val stravaAthleteDTO = networkResponse.body()
                stravaAthleteDTO?.let { currentDTO ->
                    val stravaAthlete = profileMapper.fromDtoToDomain(currentDTO)
                    DataResponse.Success(stravaAthlete)
                } ?: let {
                    DataResponse.Error(exception = IllegalStateException("No profile found"))
                }
            } else {
                DataResponse.Error(Exception(ErrorMessageConverter.getMessageToHTTPCode(networkResponse.code())))
            }
        } catch (e: Exception) {
            DataResponse.Error(Exception(ErrorMessageConverter.getMessageToException(e)))
        }
    }
}
