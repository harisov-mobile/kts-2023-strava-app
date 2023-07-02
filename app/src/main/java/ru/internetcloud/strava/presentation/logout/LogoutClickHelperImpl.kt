package ru.internetcloud.strava.presentation.logout

import kotlinx.coroutines.channels.Channel

class LogoutClickHelperImpl : LogoutClickHelper {

    private val logoutClickEventChannel = Channel<Unit>(Channel.BUFFERED)

    override fun getLogoutClickEventChannel() = logoutClickEventChannel

    override suspend fun onLogoutClick() {
        logoutClickEventChannel.send(Unit)
    }
}
