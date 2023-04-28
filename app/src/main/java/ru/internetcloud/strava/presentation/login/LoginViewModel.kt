package ru.internetcloud.strava.presentation.login

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ru.internetcloud.strava.R

class LoginViewModel(private val app: Application, savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _state: MutableLiveData<LoginState> =
        savedStateHandle.getLiveData(KEY_LOGIN_STATE, LoginState.InitialState)
    val state: LiveData<LoginState>
        get() = _state

    fun checkValidationAccordingLoginState(email: String, password: String) {
        if (!(state.value is LoginState.InitialState)) {
            checkValidation(email = email, password = password)
        }
    }

    private fun checkValidation(email: String, password: String) {
        if (isEmailValid(email) && isPasswordValid(password)) {
            _state.value = LoginState.IsValid
        } else {
            val isEmailIncorrect = !isEmailValid(email)
            val isPasswordIncorrect = !isPasswordValid(password)
            _state.value = LoginState.NotValid(
                IncorrectFields(
                    isEmailIncorrect = isEmailIncorrect,
                    isPasswordIncorrect = isPasswordIncorrect,
                    showErrorsInSnackbar = false,
                    errorsMessage = getMultiLineErrorMessage(
                        isEmailIncorrect = isEmailIncorrect,
                        isPasswordIncorrect = isPasswordIncorrect
                    ),
                    incorrectEmailMessage = getIncorrectEmailMessage(isEmailIncorrect),
                    incorrectPasswordMessage = getIncorrectPasswordMessage(isPasswordIncorrect)
                )
            )
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= VALID_PASSWORD_LENGTH
    }

    fun login(email: String, password: String) {
        checkValidation(email, password)
        if (state.value is LoginState.IsValid) {
            checkAuth(
                AuthParameters(email = email, password = password)
            )
        } else if (state.value is LoginState.NotValid) {
            _state.value = LoginState.NotValid(
                incorrectFields = (state.value as LoginState.NotValid).incorrectFields.copy(
                    showErrorsInSnackbar = true
                )
            )
        }
    }

    private fun checkAuth(authParameters: AuthParameters) {
        // здесь должна быть проверка авторизации на сервере, допустим, что авторизация всегда проходит:
        val isAuthSuccessful = true
        if (isAuthSuccessful) {
            _state.value = LoginState.Success
        } else {
            _state.value = LoginState.Error(stringResId = R.string.login_auth_not_passed)
        }
    }

    private fun getMultiLineErrorMessage(isEmailIncorrect: Boolean, isPasswordIncorrect: Boolean): String {
        var message = ""
        if (isEmailIncorrect) {
            val temp = getIncorrectEmailMessage(isEmailIncorrect)
            if (message.isBlank()) {
                message = temp
            } else {
                message = message + "\n" + temp
            }
        }

        if (isPasswordIncorrect) {
            val temp = getIncorrectPasswordMessage(isPasswordIncorrect)
            if (message.isBlank()) {
                message = temp
            } else {
                message = message + "\n" + temp
            }
        }
        return message
    }

    private fun getIncorrectEmailMessage(isEmailIncorrect: Boolean): String {
        return if (isEmailIncorrect) {
            app.getString(R.string.login_email_incorrect)
        } else {
            EMPTY_MESSAGE
        }
    }

    private fun getIncorrectPasswordMessage(isPasswordIncorrect: Boolean): String {
        return if (isPasswordIncorrect) {
            String.format(
                app.getString(R.string.login_password_incorrect),
                VALID_PASSWORD_LENGTH.toString()
            )
        } else {
            EMPTY_MESSAGE
        }
    }

    companion object {
        private const val VALID_PASSWORD_LENGTH = 8
        private const val KEY_LOGIN_STATE = "key_login_state"
        private const val EMPTY_MESSAGE = ""
    }
}
