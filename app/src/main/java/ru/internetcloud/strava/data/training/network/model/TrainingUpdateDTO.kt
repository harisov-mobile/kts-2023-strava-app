package ru.internetcloud.strava.data.training.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TrainingUpdateDTO(
    @Json(name = "id")
    val id: Long,

    @Json(name = "name")
    val name: String,

    @Json(name = "sport_type")
    val sportType: String,

    @Json(name = "description")
    val description: String,

    @Json(name = "trainer")
    val trainer: Boolean,

    @Json(name = "commute")
    val commute: Boolean,

    @Json(name = "hide_from_home")
    val hideFromHome: Boolean = false,

    @Json(name = "gear_id")
    val gearId: String = "",

    @Json(name = "start_date")
    val startDate: String,

    @Json(name = "elapsed_time")
    val elapsedTime: Int,

    @Json(name = "distance")
    val distance: Float
)
