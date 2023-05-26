package ru.internetcloud.strava.presentation.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.internetcloud.strava.data.internet.repository.InternetStatusRepositoryImpl
import ru.internetcloud.strava.domain.internet.usecase.GetInternetStatusUseCase
import ru.internetcloud.strava.presentation.logout.LogoutClickHelper
import timber.log.Timber

class MainScreenViewModel(private val app: Application) : ViewModel() {

    private val internetStatusRepository = InternetStatusRepositoryImpl()
    private val getInternetStatusUseCase = GetInternetStatusUseCase(internetStatusRepository)

    val internetConnectionAvailable: Flow<Boolean>
        get() = fetchInternetChange()

    private val onLogoutClickFlow: Flow<Unit>
        get() = LogoutClickHelper.logoutClickEventChannel.receiveAsFlow()

    private val _showLogoutDialog = MutableStateFlow(false)
    val showLogoutDialog = _showLogoutDialog.asStateFlow()

    private val navigateLogoutEventChannel = Channel<Unit>(Channel.BUFFERED)

    val navigateLogoutFlow: Flow<Unit>
        get() = navigateLogoutEventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            onLogoutClickFlow.collect {
                _showLogoutDialog.value = true
            }
        }
    }

    private fun fetchInternetChange(): Flow<Boolean> {
        return getInternetStatusUseCase.observeInternetChange(app)
    }

    fun onLogoutDialogConfirm() {
        _showLogoutDialog.value = false
        Timber.tag("rustam").d("onLogoutDialogConfirm")
        // Continue with executing the confirmed action
        navigateLogoutEventChannel.trySendBlocking(Unit)
    }

    fun onLogoutDialogDismiss() {
        _showLogoutDialog.value = false
        Timber.tag("rustam").d("Dismiss")
    }
}
