package ru.internetcloud.strava.domain.token

import kotlinx.coroutines.channels.Channel

class UnauthorizedHandler {

    val unauthorizedEventChannel = Channel<Unit>(Channel.BUFFERED)

    suspend fun onUnauthorized() {
        unauthorizedEventChannel.send(Unit)
    }
}
