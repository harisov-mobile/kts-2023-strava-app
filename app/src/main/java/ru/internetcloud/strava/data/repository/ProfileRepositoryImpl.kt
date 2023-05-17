package ru.internetcloud.strava.data.repository

import ru.internetcloud.strava.data.mapper.ProfileMapper
import ru.internetcloud.strava.data.network.api.StravaApiFactory
import ru.internetcloud.strava.domain.ProfileRepository
import ru.internetcloud.strava.domain.model.DataResponse
import ru.internetcloud.strava.domain.model.Profile

class ProfileRepositoryImpl : ProfileRepository {

    private val stravaAthleteMapper = ProfileMapper()

    override suspend fun getProfile(): DataResponse<Profile> {
        var result: DataResponse<Profile>

        try {
            val networkResponse = StravaApiFactory.profileApi.getProfile()
            if (networkResponse.isSuccessful) {
                val stravaAthleteDTO = networkResponse.body()
                result = stravaAthleteDTO?.let { currentDTO ->
                    val stravaAthlete = stravaAthleteMapper.fromDtoToDomain(currentDTO)
                    DataResponse.Success(stravaAthlete)
                } ?: let {
                    DataResponse.Error(exception = java.lang.IllegalStateException("No athlete found"))
                }
            } else {
                result = DataResponse.Error(Exception(networkResponse.errorBody()?.string().orEmpty()))
            }
        } catch (e: Exception) {
            result = DataResponse.Error(e)
        }
        return result
    }
}