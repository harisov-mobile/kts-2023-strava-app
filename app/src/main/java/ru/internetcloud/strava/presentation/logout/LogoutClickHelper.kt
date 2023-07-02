package ru.internetcloud.strava.presentation.logout

import kotlinx.coroutines.channels.Channel

interface LogoutClickHelper {
    fun getLogoutClickEventChannel(): Channel<Unit>

    suspend fun onLogoutClick()
}
