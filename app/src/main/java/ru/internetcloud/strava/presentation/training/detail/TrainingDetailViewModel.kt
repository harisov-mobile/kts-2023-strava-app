package ru.internetcloud.strava.presentation.training.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.internetcloud.strava.data.repository.ProfileRepositoryImpl
import ru.internetcloud.strava.data.repository.TrainingRepositoryImpl
import ru.internetcloud.strava.domain.model.DataResponse
import ru.internetcloud.strava.domain.model.ProfileWithTraining
import ru.internetcloud.strava.domain.usecase.GetProfileUseCase
import ru.internetcloud.strava.domain.usecase.GetTrainingUseCase
import ru.internetcloud.strava.presentation.util.UiState

class TrainingDetailViewModel(id: Long) : ViewModel() {

    private val trainingRepository = TrainingRepositoryImpl()
    private val getTrainingUseCase = GetTrainingUseCase(trainingRepository)

    private val profileRepository = ProfileRepositoryImpl()
    private val getProfileUseCase = GetProfileUseCase(profileRepository)

    private val initialState = UiState.Loading

    private val _screenState = MutableLiveData<UiState<ProfileWithTraining>>(initialState)
    val screenState: LiveData<UiState<ProfileWithTraining>>
        get() = _screenState

    init {
        fetchTraining(id)
    }

    fun fetchTraining(id: Long) {
        viewModelScope.launch {
            _screenState.value = UiState.Loading

            when (val profileDataResponse = getProfileUseCase.getProfile()) {
                is DataResponse.Success -> {
                    val profile = profileDataResponse.data
                    when (val trainingDataResponse = getTrainingUseCase.getTraining(id = id)) {
                        is DataResponse.Success -> {
                            val profileWithTraining = ProfileWithTraining(
                                profile = profile,
                                training = trainingDataResponse.data
                            )
                            _screenState.value = UiState.Success(data = profileWithTraining)
                        }
                        is DataResponse.Error -> {
                            _screenState.value = UiState.Error(exception = trainingDataResponse.exception)
                        }
                    }
                }
                is DataResponse.Error -> {
                    _screenState.value = UiState.Error(exception = profileDataResponse.exception)
                }
            }
        }
    }
}
