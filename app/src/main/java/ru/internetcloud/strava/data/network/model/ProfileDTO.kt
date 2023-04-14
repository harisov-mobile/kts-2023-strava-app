package ru.internetcloud.strava.data.network.model

data class ProfileDTO(
    val id: Long,
    val firstname: String?,
    val lastname: String?,
    val city: String?,
    val state: String?,
    val country: String?,
    val sex: String?,
    val profile_medium: String?,
    val profile: String?,
    val friend_count: Int?,
    val follower_count: Int?
)
