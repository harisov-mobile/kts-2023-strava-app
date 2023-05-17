package ru.internetcloud.strava.presentation.login

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class IncorrectFields(
    val isEmailIncorrect: Boolean,
    val isPasswordIncorrect: Boolean,
    val showErrorsInSnackbar: Boolean,
    val errorsMessage: String,
    val incorrectEmailMessage: String,
    val incorrectPasswordMessage: String
) : Parcelable
