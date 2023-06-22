package ru.internetcloud.strava.presentation.main

import android.os.Bundle
import androidx.annotation.StringRes

sealed interface MainScreenEvent {

    data class NavigateToLogout(val args: Bundle?) : MainScreenEvent

    data class ShowMessage(@StringRes val messageRes: Int) : MainScreenEvent
}
