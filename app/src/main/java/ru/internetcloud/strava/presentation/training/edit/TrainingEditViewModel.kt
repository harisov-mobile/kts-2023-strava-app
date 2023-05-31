package ru.internetcloud.strava.presentation.training.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.internetcloud.strava.data.training.repository.TrainingRepositoryImpl
import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.common.model.Source
import ru.internetcloud.strava.domain.training.model.Training
import ru.internetcloud.strava.domain.training.usecase.GetTrainingUseCase
import ru.internetcloud.strava.presentation.util.UiState
import timber.log.Timber

class TrainingEditViewModel(id: Long, savedStateHandle: SavedStateHandle) : ViewModel() {

    private val trainingRepository = TrainingRepositoryImpl()
    private val getTrainingUseCase = GetTrainingUseCase(trainingRepository)

    private val initialState =
        savedStateHandle.get<UiState<Training>>(KEY_TRAINING_EDIT_STATE) ?: UiState.Loading

    private val _screenState = MutableStateFlow(initialState)
    val screenState = _screenState.asStateFlow()

    init {
        fetchTraining(id)
    }

    fun fetchTraining(id: Long) {
        viewModelScope.launch {
            _screenState.value = UiState.Loading

            when (val trainingDataResponse = getTrainingUseCase.getTraining(id = id)) {
                is DataResponse.Success -> {
                    val training = trainingDataResponse.data

                    if (trainingDataResponse.source is Source.LocalCache) {
                        _screenState.value = UiState.Error(exception = IllegalStateException("Network error"))
                    } else {
                        Timber.tag("rustam").d("training = $training")

                        _screenState.value = UiState.Success(
                            data = training,
                            source = trainingDataResponse.source
                        )
                    }
                }

                is DataResponse.Error -> {
                    _screenState.value = UiState.Error(exception = trainingDataResponse.exception)
                }
            }
        }
    }

    fun handleEvent(editTrainingEvent: EditTrainingEvent) {
        val oldTraining = (_screenState.value as UiState.Success).data

        when (editTrainingEvent) {
            is EditTrainingEvent.OnNameChange -> {
                setScreenState(oldTraining.copy(name = editTrainingEvent.name))
            }

            is EditTrainingEvent.OnDescriptionChange -> {
                setScreenState(oldTraining.copy(description = editTrainingEvent.description))
            }

            is EditTrainingEvent.OnSportTypeChange -> {
                setScreenState(oldTraining.copy(sportType = editTrainingEvent.sportType))
            }

            is EditTrainingEvent.OnStartDateChange -> {
                setScreenState(oldTraining.copy(startDate = editTrainingEvent.startDate))
            }

            is EditTrainingEvent.OnDurationChange -> {
                setScreenState(
                    oldTraining.copy(
                        movingTime = editTrainingEvent.durationTime,
                        elapsedTime = editTrainingEvent.durationTime
                    )
                )
            }

            is EditTrainingEvent.OnDistanceChange -> {
                setScreenState(oldTraining.copy(distance = editTrainingEvent.distance))
            }
        }
    }

    private fun setScreenState(training: Training) {
        _screenState.value = UiState.Success(
            data = training,
            source = (_screenState.value as UiState.Success).source,
            isChanged = true
        )
    }

    companion object {
        private const val KEY_TRAINING_EDIT_STATE = "key_training_edit_state"
    }
}
