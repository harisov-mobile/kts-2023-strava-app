package ru.internetcloud.strava.data.auth.network.model

data class TokensModel(
    val accessToken: String,
    val refreshToken: String,
    val idToken: String
)
