package ru.internetcloud.strava.presentation.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class LoginViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _state = savedStateHandle.getLiveData(KEY_LOGIN_STATE, LoginState())
    val state: LiveData<LoginState>
        get() = _state

    fun setEmail(email: String) {
        _state.value = _state.value?.copy(email = email) ?: LoginState(email = email)
        checkValidation()
    }

    fun setPassword(password: String) {
        _state.value = _state.value?.copy(password = password) ?: LoginState(password = password)
        checkValidation()
    }

    fun checkValidation() {
        _state.value?.let { currentState ->
            if (currentState.password.length >= VALID_PASSWORD_LENGTH &&
                Patterns.EMAIL_ADDRESS.matcher(currentState.email).matches()) {
                _state.value = currentState.copy(loginEnabled = true)
            } else {
                _state.value = currentState.copy(loginEnabled = false)
            }
        }
    }

    companion object {
        private const val VALID_PASSWORD_LENGTH = 8
        private const val KEY_LOGIN_STATE = "key_login_state"
    }
}
