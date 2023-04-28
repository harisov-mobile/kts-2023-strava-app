package ru.internetcloud.strava.presentation.login

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class IncorrectFields(
    val isEmailIncorrect: Boolean = false,
    val isPasswordIncorrect: Boolean = false,
    val showErrorsInSnackbar: Boolean = false,
    val errorsMessage: String,
    val incorrectEmailMessage: String,
    val incorrectPasswordMessage: String
) : Parcelable
