package ru.internetcloud.strava.data.profile.network.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query
import ru.internetcloud.strava.data.profile.network.model.ProfileDTO

interface ProfileApi {
    @GET("api/v3/athlete")
    suspend fun getProfile(): Response<ProfileDTO>

    @PUT("api/v3/athlete")
    suspend fun saveWeight(
        @Query("weight") weight: Double
    ): Response<ProfileDTO>
}
