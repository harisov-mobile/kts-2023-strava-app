package ru.internetcloud.strava.data.logout.network.api

import retrofit2.Response
import retrofit2.http.POST
import ru.internetcloud.strava.data.logout.network.model.LogoutAnswerDTO

interface LogoutApi {

    @POST("oauth/deauthorize")
    suspend fun logout(): Response<LogoutAnswerDTO>
}
