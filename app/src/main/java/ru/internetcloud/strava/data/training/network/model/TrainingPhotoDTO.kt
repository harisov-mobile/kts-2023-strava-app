package ru.internetcloud.strava.data.training.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TrainingPhotoDTO(
    @Json(name = "unique_id")
    val id: String,

    @Json(name = "urls")
    val urls: PhotoUrlDTO,

    @Json(name = "default_photo")
    val defaultPhoto: Boolean
)
