package ru.internetcloud.strava.domain.token

interface TokenRepository {

    fun isAuthorized(): Boolean
}
