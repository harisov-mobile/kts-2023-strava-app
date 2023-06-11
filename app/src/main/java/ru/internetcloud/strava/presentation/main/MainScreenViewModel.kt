package ru.internetcloud.strava.presentation.main

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.internetcloud.strava.R
import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.internet.usecase.GetInternetStatusUseCase
import ru.internetcloud.strava.domain.logout.usecase.LogoutUseCase
import ru.internetcloud.strava.domain.token.UnauthorizedHandler
import ru.internetcloud.strava.presentation.logout.LogoutClickHelper

class MainScreenViewModel(
    private val app: Application,
    private val keyMessage: String,
    private val getInternetStatusUseCase: GetInternetStatusUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    val internetConnectionAvailable: Flow<Boolean>
        get() = fetchInternetChange()

    private val onLogoutClickFlow: Flow<Unit>
        get() = LogoutClickHelper.logoutClickEventChannel.receiveAsFlow()

    private val _showLogoutDialog = MutableStateFlow(false)
    val showLogoutDialog = _showLogoutDialog.asStateFlow()

    private val screenEventChannel = Channel<MainScreenEvent>(Channel.BUFFERED)
    val screenEventFlow: Flow<MainScreenEvent>
        get() = screenEventChannel.receiveAsFlow()

    private val onUnauthorizedEventFlow: Flow<Unit>
        get() = UnauthorizedHandler.unauthorizedEventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            onLogoutClickFlow.collect {
                _showLogoutDialog.value = true
            }
        }

        viewModelScope.launch {
            onUnauthorizedEventFlow.collect {
                onUnauthorized()
            }
        }
    }

    private fun fetchInternetChange(): Flow<Boolean> {
        return getInternetStatusUseCase.observeInternetChange(app)
    }

    fun onLogoutDialogConfirm() {
        _showLogoutDialog.value = false

        viewModelScope.launch {
            val args = Bundle().apply {
                putInt(keyMessage, R.string.auth_after_logout)
            }

            when (val logoutDataResponse = logoutUseCase.logout()) {
                is DataResponse.Success -> {
                    screenEventChannel.trySend(MainScreenEvent.NavigateToLogout(args = args))
                }

                is DataResponse.Error -> {
                    screenEventChannel.trySend(MainScreenEvent.NavigateToLogout(args = args))
                }
            }
        }
    }

    fun onLogoutDialogDismiss() {
        _showLogoutDialog.value = false
    }

    fun onUnauthorized() {
        val args = Bundle().apply {
            putInt(keyMessage, R.string.auth_try_again)
        }
        screenEventChannel.trySend(MainScreenEvent.NavigateToLogout(args = args))
    }
}
