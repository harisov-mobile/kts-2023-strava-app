package ru.internetcloud.strava.presentation.login

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AuthParameters(
    val email: String = "",
    val password: String = ""
) : Parcelable
