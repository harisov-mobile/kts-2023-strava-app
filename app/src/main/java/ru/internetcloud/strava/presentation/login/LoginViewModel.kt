package ru.internetcloud.strava.presentation.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ru.internetcloud.strava.R

class LoginViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

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
            _state.value = LoginState.NotValid(
                IncorrectFields(
                    isEmailIncorrect = !isEmailValid(email),
                    isPasswordIncorrect = !isPasswordValid(password),
                    validPasswordLengh = VALID_PASSWORD_LENGTH,
                    showErrorsInSnackbar = false
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
            _state.value = LoginState.Error(stringResId = R.string.auth_not_passed)
        }
    }

    companion object {
        private const val VALID_PASSWORD_LENGTH = 8
        private const val KEY_LOGIN_STATE = "key_login_state"
    }
}
