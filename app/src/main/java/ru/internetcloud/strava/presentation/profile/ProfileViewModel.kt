package ru.internetcloud.strava.presentation.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.profile.model.Profile
import ru.internetcloud.strava.domain.profile.usecase.GetProfileUseCase
import ru.internetcloud.strava.presentation.util.UiState

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val initialState = savedStateHandle.get<UiState<Profile>>(KEY_PROFILE_STATE) ?: UiState.Loading

    private val _screenState = MutableStateFlow(initialState)
    val screenState = _screenState.asStateFlow()

    init {
        fetchProfile()
    }

    fun fetchProfile() {
        viewModelScope.launch {
            _screenState.value = UiState.Loading

            when (val dataResponse = getProfileUseCase.getProfile()) {
                is DataResponse.Success -> {
                    _screenState.value = UiState.Success(data = dataResponse.data, source = dataResponse.source)
                }
                is DataResponse.Error -> {
                    _screenState.value = UiState.Error(exception = dataResponse.exception)
                }
            }
        }
    }

    companion object {
        private const val KEY_PROFILE_STATE = "key_profile_state"
    }
}
