package ru.internetcloud.strava.data.auth.network.model

data class TokensModel(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val idToken: String? = null
)
