package ru.internetcloud.strava.domain.token

import kotlinx.coroutines.channels.Channel

object UnauthorizedHandler {

    val unauthorizedEventChannel = Channel<Unit>(Channel.BUFFERED)

    suspend fun onUnauthorized() {
        unauthorizedEventChannel.send(Unit)
    }
}
