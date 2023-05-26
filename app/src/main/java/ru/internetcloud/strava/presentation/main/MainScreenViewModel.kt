package ru.internetcloud.strava.presentation.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.internetcloud.strava.data.internet.repository.InternetStatusRepositoryImpl
import ru.internetcloud.strava.data.logout.repository.LogoutRepositoryImpl
import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.internet.usecase.GetInternetStatusUseCase
import ru.internetcloud.strava.domain.logout.usecase.LogoutUseCase
import ru.internetcloud.strava.presentation.logout.LogoutClickHelper
import timber.log.Timber

class MainScreenViewModel(private val app: Application) : ViewModel() {

    private val internetStatusRepository = InternetStatusRepositoryImpl()
    private val getInternetStatusUseCase = GetInternetStatusUseCase(internetStatusRepository)

    private val logoutRepository = LogoutRepositoryImpl()
    private val logoutUseCase = LogoutUseCase(logoutRepository)

    val internetConnectionAvailable: Flow<Boolean>
        get() = fetchInternetChange()

    private val onLogoutClickFlow: Flow<Unit>
        get() = LogoutClickHelper.logoutClickEventChannel.receiveAsFlow()

    private val _showLogoutDialog = MutableStateFlow(false)
    val showLogoutDialog = _showLogoutDialog.asStateFlow()

    private val screenEventChannel = Channel<MainScreenEvent>(Channel.BUFFERED)
    val screenEventFlow: Flow<MainScreenEvent>
        get() = screenEventChannel.receiveAsFlow()

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

        viewModelScope.launch {
            when (val logoutDataResponse = logoutUseCase.logout()) {
                is DataResponse.Success -> {
                    Timber.tag("rustam").d("logoutDataResponse, accessToken = ${logoutDataResponse.data.accessToken}")
                    screenEventChannel.trySend(MainScreenEvent.NavigateToLogout)
                }
                is DataResponse.Error -> {
                    Timber.tag("rustam").d("logout error, ${logoutDataResponse.exception.message}")
                    // screenEventChannel.trySend(MainScreenEvent.ShowMessage(R.string.logout_error))
                    screenEventChannel.trySend(MainScreenEvent.NavigateToLogout)
                }
            }
        }
    }

    fun onLogoutDialogDismiss() {
        _showLogoutDialog.value = false
        Timber.tag("rustam").d("Dismiss")
    }
}
