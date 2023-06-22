package ru.internetcloud.strava.domain.token.model

data class TokensModel(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val idToken: String? = null
)
