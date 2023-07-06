package ru.internetcloud.strava.data.training.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhotoUrlDTO(
    @Json(name = QUERY_VALUE_SIZE)
    val url: String
) {
    companion object {
        const val QUERY_VALUE_SIZE = "400"
    }
}
