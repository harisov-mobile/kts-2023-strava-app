package ru.internetcloud.strava.presentation.logout

import kotlinx.coroutines.channels.Channel

class LogoutClickHelper {

    val logoutClickEventChannel = Channel<Unit>(Channel.BUFFERED)

    suspend fun onLogoutClick() {
        logoutClickEventChannel.send(Unit)
    }
}
