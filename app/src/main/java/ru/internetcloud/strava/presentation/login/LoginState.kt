package ru.internetcloud.strava.presentation.login

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

sealed interface LoginState : Parcelable {

    @Parcelize
    object InitialState : LoginState

    @Parcelize
    data class NotValid(val incorrectFields: IncorrectFields) : LoginState

    @Parcelize
    object IsValid : LoginState

    @Parcelize
    object Success : LoginState

    @Parcelize
    data class Error(@StringRes val stringResId: Int) : LoginState
}
