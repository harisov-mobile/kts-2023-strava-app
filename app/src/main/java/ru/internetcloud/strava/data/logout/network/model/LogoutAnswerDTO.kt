package ru.internetcloud.strava.data.logout.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LogoutAnswerDTO(
    @Json(name = "access_token")
    val accessToken: String?
)
