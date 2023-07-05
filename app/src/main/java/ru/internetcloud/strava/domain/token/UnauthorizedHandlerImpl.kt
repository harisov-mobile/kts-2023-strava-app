package ru.internetcloud.strava.domain.token

import kotlinx.coroutines.channels.Channel

class UnauthorizedHandlerImpl : UnauthorizedHandler {

    private val unauthorizedEventChannel = Channel<Unit>(Channel.BUFFERED)

    override fun getUnauthorizedEventChannel() = unauthorizedEventChannel

    override suspend fun onUnauthorized() {
        unauthorizedEventChannel.send(Unit)
    }
}
