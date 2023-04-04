package ru.internetcloud.strava.presentation.login

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginState(
    val email: String = "",
    val password: String = "",
    val loginEnabled: Boolean = false
) : Parcelable
