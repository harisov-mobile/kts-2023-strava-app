package ru.internetcloud.strava.presentation.login

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class IncorrectFields(
    val isEmailIncorrect: Boolean = false,
    val isPasswordIncorrect: Boolean = false,
    val validPasswordLengh: Int = 0,
    val showErrorsInSnackbar: Boolean = false
) : Parcelable
