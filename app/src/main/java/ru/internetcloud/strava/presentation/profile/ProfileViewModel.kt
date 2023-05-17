package ru.internetcloud.strava.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.internetcloud.strava.data.repository.ProfileRepositoryImpl
import ru.internetcloud.strava.domain.model.DataResponse
import ru.internetcloud.strava.domain.model.Profile
import ru.internetcloud.strava.domain.usecase.GetProfileUseCase
import ru.internetcloud.strava.presentation.util.UiState

class ProfileViewModel : ViewModel() {

    private val profileRepository = ProfileRepositoryImpl()
    private val getProfileUseCase = GetProfileUseCase(profileRepository)

    private val initialState = UiState.Loading

    private val _screenState = MutableLiveData<UiState<Profile>>(initialState)
    val screenState: LiveData<UiState<Profile>>
        get() = _screenState

    init {
        fetchProfile()
    }

    fun fetchProfile() {
        viewModelScope.launch {
            _screenState.value = UiState.Loading

            val dataResponse = getProfileUseCase.getProfile()
            when (dataResponse) {
                is DataResponse.Success -> {
                    _screenState.value = UiState.Success(data = dataResponse.data)
                }
                is DataResponse.Error -> {
                    _screenState.value = UiState.Error(exception = dataResponse.exception)
                }
            }
        }
    }
}
