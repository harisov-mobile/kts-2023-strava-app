package ru.internetcloud.strava.data.profile.cache.model

data class ProfileDbModel(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val city: String,
    val state: String,
    val country: String,
    val sex: String,
    val imageUrlMedium: String,
    val imageUrl: String,
    val friendCount: Int,
    val followerCount: Int
)
