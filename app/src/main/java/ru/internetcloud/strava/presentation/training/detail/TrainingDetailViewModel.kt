package ru.internetcloud.strava.presentation.training.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.internetcloud.strava.R
import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.profile.model.ProfileWithTraining
import ru.internetcloud.strava.domain.profile.usecase.GetProfileUseCase
import ru.internetcloud.strava.domain.training.usecase.DeleteTrainingUseCase
import ru.internetcloud.strava.domain.training.usecase.GetTrainingUseCase
import ru.internetcloud.strava.presentation.util.UiState
import ru.internetcloud.strava.presentation.util.toStringVs

class TrainingDetailViewModel(
    id: Long,
    private val getTrainingUseCase: GetTrainingUseCase,
    private val deleteTrainingUseCase: DeleteTrainingUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val initialState =
        savedStateHandle.get<UiState<ProfileWithTraining>>(KEY_TRAINING_DETAIL_STATE) ?: UiState.Loading

    private val _screenState = MutableStateFlow(initialState)
    val screenState = _screenState.asStateFlow()

    private val screenEventChannel = Channel<TrainingDetailScreenEvent>(Channel.BUFFERED)
    val screenEventFlow: Flow<TrainingDetailScreenEvent>
        get() = screenEventChannel.receiveAsFlow()

    init {
        fetchTraining(id, isChanged = false)
    }

    fun fetchTraining(id: Long, isChanged: Boolean) {
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
                            _screenState.value = UiState.Success(
                                data = profileWithTraining,
                                source = trainingDataResponse.source,
                                isChanged = isChanged
                            )
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

    fun deleteTraining() {
        viewModelScope.launch {
            val state = _screenState.value
            if (state is UiState.Success) {
                when (val deleteDataResponse = deleteTrainingUseCase.deleteTraining(
                    state.data.training.id
                )) {
                    is DataResponse.Success -> {
                        screenEventChannel.trySend(
                            TrainingDetailScreenEvent.ShowMessage(R.string.training_deleted.toStringVs())
                        )
                        screenEventChannel.trySend(
                            TrainingDetailScreenEvent
                                .NavigateBack
                        )
                    }

                    is DataResponse.Error -> {
                        screenEventChannel
                            .trySend(
                                TrainingDetailScreenEvent.ShowMessage(
                                    (R.string.training_can_not_delete_training_with_arg
                                        .toStringVs(deleteDataResponse.exception.message.toString()))
                                )
                            )
                    }
                }
            } else {
                screenEventChannel
                    .trySend(
                        TrainingDetailScreenEvent.ShowMessage(
                            R.string.training_can_not_delete_training.toStringVs()
                        )
                    )
            }
        }
    }

    companion object {
        private const val KEY_TRAINING_DETAIL_STATE = "key_training_detail_state"
    }
}
