package ru.internetcloud.strava.presentation.auth.model

data class TokensModel(
    val accessToken: String,
    val refreshToken: String,
    val idToken: String
)
