package ru.internetcloud.strava.data.profile.network.api

import retrofit2.Response
import retrofit2.http.GET
import ru.internetcloud.strava.data.profile.network.model.ProfileDTO

interface ProfileApi {

    @GET("athlete")
    suspend fun getProfile(): Response<ProfileDTO>
}
