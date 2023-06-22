package ru.internetcloud.strava.presentation.logout

import kotlinx.coroutines.channels.Channel

object LogoutClickHelper {

    val logoutClickEventChannel = Channel<Unit>(Channel.BUFFERED)

    suspend fun onLogoutClick() {
        logoutClickEventChannel.send(Unit)
    }
}
