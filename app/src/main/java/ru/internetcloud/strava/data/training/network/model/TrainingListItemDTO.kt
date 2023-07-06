package ru.internetcloud.strava.data.training.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TrainingListItemDTO(
    @Json(name = "id")
    val id: Long,

    @Json(name = "name")
    val name: String,

    @Json(name = "distance")
    val distance: Float,

    @Json(name = "moving_time")
    val movingTime: Int,

    @Json(name = "sport_type")
    val sport: String,

    @Json(name = "start_date_local")
    val startDate: String
)
