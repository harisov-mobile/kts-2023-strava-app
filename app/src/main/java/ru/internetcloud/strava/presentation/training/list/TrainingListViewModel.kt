package ru.internetcloud.strava.presentation.training.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.internetcloud.strava.data.profile.repository.ProfileRepositoryImpl
import ru.internetcloud.strava.data.training.repository.TrainingRepositoryImpl
import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.profile.model.ProfileWithTrainingList
import ru.internetcloud.strava.domain.profile.usecase.GetProfileUseCase
import ru.internetcloud.strava.domain.training.usecase.GetTrainingsUseCase
import ru.internetcloud.strava.presentation.util.UiState

class TrainingListViewModel : ViewModel() {

    private val trainingRepository = TrainingRepositoryImpl()
    private val getTrainingsUseCase = GetTrainingsUseCase(trainingRepository)

    private val profileRepository = ProfileRepositoryImpl()
    private val getProfileUseCase = GetProfileUseCase(profileRepository)

    private val initialState = UiState.Loading

    private val _screenState = MutableLiveData<UiState<ProfileWithTrainingList>>(initialState)
    val screenState: LiveData<UiState<ProfileWithTrainingList>>
        get() = _screenState

    init {
        fetchStravaActivities()
    }

    fun fetchStravaActivities() {
        viewModelScope.launch {
            _screenState.value = UiState.Loading

            var profileWithTrainings: ProfileWithTrainingList

            when (val profileDataResponse = getProfileUseCase.getProfile()) {
                is DataResponse.Success -> {
                    profileWithTrainings = ProfileWithTrainingList(
                        profile = profileDataResponse.data,
                        trainingList = emptyList()
                    )
                    when (val trainingsDataResponse = getTrainingsUseCase.getTrainings(page = 1)) {
                        is DataResponse.Success -> {
                            val list = trainingsDataResponse.data.toList()
                            if (list.isEmpty()) {
                                _screenState.value = UiState.EmptyData
                            } else {
                                profileWithTrainings = profileWithTrainings.copy(
                                    trainingList = trainingsDataResponse.data.toList()
                                )
                                _screenState.value = UiState.Success(data = profileWithTrainings)
                            }
                        }
                        is DataResponse.Error -> {
                            _screenState.value = UiState.Error(exception = trainingsDataResponse.exception)
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
