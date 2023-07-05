package ru.internetcloud.strava.domain.token

import kotlinx.coroutines.channels.Channel

interface UnauthorizedHandler {

    fun getUnauthorizedEventChannel(): Channel<Unit>
    suspend fun onUnauthorized()
}
