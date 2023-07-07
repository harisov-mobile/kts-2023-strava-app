package ru.internetcloud.strava.data.profile.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProfWeight(
    @Json(name = "id")
    val id: Long
)
