package ru.internetcloud.strava.presentation.auth

object TokenStorage {
    var accessToken: String? = null
    var refreshToken: String? = null
    var idToken: String? = null
}
