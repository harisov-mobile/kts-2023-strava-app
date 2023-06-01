package ru.internetcloud.strava.presentation.training.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.util.Date
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.internetcloud.strava.data.training.repository.TrainingRepositoryImpl
import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.common.model.Source
import ru.internetcloud.strava.domain.training.model.Training
import ru.internetcloud.strava.domain.training.usecase.GetTrainingUseCase
import ru.internetcloud.strava.domain.training.usecase.UpdateTrainingUseCase
import ru.internetcloud.strava.presentation.util.UiState
import timber.log.Timber

class TrainingEditViewModel(
    id: Long,
    editMode: EditMode,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val trainingRepository = TrainingRepositoryImpl()
    private val getTrainingUseCase = GetTrainingUseCase(trainingRepository)
    private val updateTrainingUseCase = UpdateTrainingUseCase(trainingRepository)

    private val initialState =
        savedStateHandle.get<UiState<Training>>(KEY_TRAINING_EDIT_STATE) ?: UiState.Loading

    private val _screenState = MutableStateFlow(initialState)
    val screenState = _screenState.asStateFlow()

    private val screenEventChannel = Channel<TrainingEditScreenEvent>(Channel.BUFFERED)
    val screenEventFlow: Flow<TrainingEditScreenEvent>
        get() = screenEventChannel.receiveAsFlow()

    init {
        when (editMode) {
            EditMode.Add -> createTraining()
            EditMode.Edit -> fetchTraining(id)
        }
    }

    private fun createTraining() {
        val newTraining = Training(
            id = EMPTY_ID,
            name = "",
            distance = 0f,
            movingTime = 0,
            elapsedTime = 0,
            type = INITIAL_SPORT_TYPE,
            sportType = INITIAL_SPORT_TYPE,
            startDate = Date(),
            description = "",
            trainer = false,
            commute = false
        )

        _screenState.value = UiState.Success(
            data = newTraining,
            source = Source.RemoteApi
        )
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

    fun saveTraining() {
        viewModelScope.launch {
            val training = (_screenState.value as UiState.Success).data

            _screenState.value = UiState.Success(
                data = training,
                source = (_screenState.value as UiState.Success).source,
                isChanged = (_screenState.value as UiState.Success).isChanged,
                saving = true
            )

            when (val trainingDataResponse = updateTrainingUseCase.updateTraining(training)) {
                is DataResponse.Success -> {
                    _screenState.value = UiState.Success(
                        data = training,
                        source = (_screenState.value as UiState.Success).source,
                        isChanged = false,
                        saving = false
                    )
                    screenEventChannel.trySend(TrainingEditScreenEvent.NavigateToTrainingDetail(id = training.id))
                }

                is DataResponse.Error -> {
                    _screenState.value = UiState.Success(
                        data = training,
                        source = (_screenState.value as UiState.Success).source,
                        isChanged = (_screenState.value as UiState.Success).isChanged,
                        saving = false
                    )
                    screenEventChannel
                        .trySend(
                            TrainingEditScreenEvent.ShowMessage(trainingDataResponse.exception.message.toString())
                        )
                }
            }
        }
    }

    companion object {
        private const val KEY_TRAINING_EDIT_STATE = "key_training_edit_state"

        private const val EMPTY_ID = -1L
        private const val INITIAL_SPORT_TYPE = "Run"
    }
}
