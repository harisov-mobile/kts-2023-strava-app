package ru.internetcloud.strava.presentation.logout

import android.app.Application
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import net.openid.appauth.AuthorizationService
import ru.internetcloud.strava.data.auth.network.AuthRepository

class LogoutViewModel(private val application: Application) : ViewModel() {

    private val authRepository = AuthRepository()
    private val authService: AuthorizationService = AuthorizationService(application)

    private val logoutPageEventChannel = Channel<Intent>(Channel.BUFFERED)
    private val logoutCompletedEventChannel = Channel<Unit>(Channel.BUFFERED)

    val logoutPageFlow: Flow<Intent>
        get() = logoutPageEventChannel.receiveAsFlow()

    val logoutCompletedFlow: Flow<Unit>
        get() = logoutCompletedEventChannel.receiveAsFlow()

    fun logout() {
        val customTabsIntent = CustomTabsIntent.Builder().build()

        val logoutRequest = authRepository.getEndSessionRequest()

        val logoutPageIntent = authService.getEndSessionRequestIntent(
            logoutRequest,
            customTabsIntent
        )

        logoutPageEventChannel.trySendBlocking(logoutPageIntent)
    }

    fun webLogoutComplete() {
        authRepository.logout()
        logoutCompletedEventChannel.trySendBlocking(Unit)
    }

    override fun onCleared() {
        super.onCleared()
        authService.dispose()
    }
}
