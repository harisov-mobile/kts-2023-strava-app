package ru.internetcloud.strava.presentation.main

import androidx.annotation.StringRes

sealed interface MainScreenEvent {

    object NavigateToLogout : MainScreenEvent

    data class ShowMessage(@StringRes val messageRes: Int) : MainScreenEvent
}
