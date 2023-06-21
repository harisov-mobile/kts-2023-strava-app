package ru.internetcloud.strava.domain.token

interface TokenRepository {

    suspend fun isAuthorized(): Boolean
}
