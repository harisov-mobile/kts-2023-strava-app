package ru.internetcloud.strava.data.profile.repository

import ru.internetcloud.strava.data.common.StravaApiFactory
import ru.internetcloud.strava.data.profile.mapper.ProfileMapper
import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.profile.ProfileRepository
import ru.internetcloud.strava.domain.profile.model.Profile

class ProfileRepositoryImpl : ProfileRepository {

    private val stravaAthleteMapper = ProfileMapper()

    override suspend fun getProfile(): DataResponse<Profile> {
        return try {
            val networkResponse = StravaApiFactory.profileApi.getProfile()
            if (networkResponse.isSuccessful) {
                val stravaAthleteDTO = networkResponse.body()
                stravaAthleteDTO?.let { currentDTO ->
                    val stravaAthlete = stravaAthleteMapper.fromDtoToDomain(currentDTO)
                    DataResponse.Success(stravaAthlete)
                } ?: let {
                    DataResponse.Error(exception = IllegalStateException("No athlete found"))
                }
            } else {
                DataResponse.Error(Exception(networkResponse.errorBody()?.string().orEmpty()))
            }
        } catch (e: Exception) {
            DataResponse.Error(e)
        }
    }
}
