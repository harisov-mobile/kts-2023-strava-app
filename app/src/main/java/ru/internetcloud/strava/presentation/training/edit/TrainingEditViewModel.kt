package ru.internetcloud.strava.presentation.training.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.util.Calendar
import java.util.Date
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.common.model.Source
import ru.internetcloud.strava.domain.common.util.DateConverter
import ru.internetcloud.strava.domain.training.model.Training
import ru.internetcloud.strava.domain.training.usecase.AddTrainingUseCase
import ru.internetcloud.strava.domain.training.usecase.GetTrainingUseCase
import ru.internetcloud.strava.domain.training.usecase.UpdateTrainingUseCase
import ru.internetcloud.strava.presentation.util.UiState

class TrainingEditViewModel(
    id: Long,
    private val editMode: EditMode,
    private val getTrainingUseCase: GetTrainingUseCase,
    private val updateTrainingUseCase: UpdateTrainingUseCase,
    private val addTrainingUseCase: AddTrainingUseCase,
    private val dateConverter: DateConverter,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

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
            sport = INITIAL_SPORT_TYPE,
            startDate = Date(),
            description = "",
            trainer = false,
            commute = false,
            photoUrls = emptyList()
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
                    if (trainingDataResponse.source is Source.LocalCache) {
                        _screenState.value = UiState.Error(exception = IllegalStateException("Network error"))
                    } else {
                        _screenState.value = UiState.Success(
                            data = trainingDataResponse.data,
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
        val state = _screenState.value
        if (state is UiState.Success) {
            val oldTraining = state.data

            when (editTrainingEvent) {
                is EditTrainingEvent.OnNameChange -> {
                    setScreenState(oldTraining.copy(name = editTrainingEvent.name))
                }

                is EditTrainingEvent.OnDescriptionChange -> {
                    setScreenState(oldTraining.copy(description = editTrainingEvent.description))
                }

                is EditTrainingEvent.OnSportTypeChange -> {
                    setScreenState(oldTraining.copy(sport = editTrainingEvent.sport))
                }

                is EditTrainingEvent.OnStartDateChange -> {
                    val calendar = Calendar.getInstance()
                    calendar.time = oldTraining.startDate
                    val hour = calendar[Calendar.HOUR_OF_DAY]
                    val minute = calendar[Calendar.MINUTE]
                    setScreenState(
                        oldTraining.copy(
                            startDate = dateConverter.getDate(
                                editTrainingEvent.year,
                                editTrainingEvent.month,
                                editTrainingEvent.day,
                                hour,
                                minute
                            )
                        )
                    )
                }

                is EditTrainingEvent.OnStartTimeChange -> {
                    val calendar = Calendar.getInstance()
                    calendar.time = oldTraining.startDate
                    val year = calendar[Calendar.YEAR]
                    val month = calendar[Calendar.MONTH]
                    val day = calendar[Calendar.DAY_OF_MONTH]
                    setScreenState(
                        oldTraining.copy(
                            startDate = dateConverter.getDate(
                                year,
                                month,
                                day,
                                editTrainingEvent.hour,
                                editTrainingEvent.minute
                            )
                        )
                    )
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
    }

    private fun setScreenState(training: Training) {
        val state = _screenState.value
        if (state is UiState.Success) {
            _screenState.value = UiState.Success(
                data = training,
                source = state.source,
                isChanged = true
            )
        }
    }

    fun saveTraining() {
        viewModelScope.launch {
            val state = _screenState.value
            if (state is UiState.Success) {
                val training = state.data

                _screenState.value = UiState.Success(
                    data = training,
                    source = state.source,
                    isChanged = state.isChanged,
                    saving = true
                )

                val trainingDataResponse = when (editMode) {
                    EditMode.Add -> addTrainingUseCase.addTraining(training)
                    EditMode.Edit -> updateTrainingUseCase.updateTraining(training)
                }

                when (trainingDataResponse) {
                    is DataResponse.Success -> {
                        _screenState.value = UiState.Success(
                            data = trainingDataResponse.data,
                            source = state.source,
                            isChanged = false,
                            saving = false
                        )
                        screenEventChannel.trySend(TrainingEditScreenEvent.NavigateBackWithRefresh)
                    }

                    is DataResponse.Error -> {
                        _screenState.value = UiState.Success(
                            data = training,
                            source = state.source,
                            isChanged = state.isChanged,
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
    }

    companion object {
        private const val KEY_TRAINING_EDIT_STATE = "key_training_edit_state"

        private const val EMPTY_ID = -1L
        private const val INITIAL_SPORT_TYPE = "Run"
    }
}
