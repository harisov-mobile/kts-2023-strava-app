package ru.internetcloud.strava.data.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TrainingDTO(
    @Json(name = "id")
    val id: Long,

    @Json(name = "name")
    val name: String,

    @Json(name = "distance")
    val distance: Float,

    @Json(name = "moving_time")
    val movingTime: Int,

    @Json(name = "type")
    val type: String,

    @Json(name = "start_date")
    val startDate: String,

    @Json(name = "description")
    val description: String?
)
