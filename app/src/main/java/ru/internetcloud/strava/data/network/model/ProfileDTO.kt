package ru.internetcloud.strava.data.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProfileDTO(
    @Json(name = "id")
    val id: Long,

    @Json(name = "firstname")
    val firstname: String?,

    @Json(name = "lastname")
    val lastname: String?,

    @Json(name = "city")
    val city: String?,

    @Json(name = "state")
    val state: String?,

    @Json(name = "country")
    val country: String?,

    @Json(name = "sex")
    val sex: String?,

    @Json(name = "profile_medium")
    val profileMedium: String?,

    @Json(name = "profile")
    val profile: String?,

    @Json(name = "friend_count")
    val friendCount: Int?,

    @Json(name = "follower_count")
    val followerCount: Int?
)
