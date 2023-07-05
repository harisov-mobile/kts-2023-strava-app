package ru.internetcloud.strava.presentation.training.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkivanov.mvikotlin.core.binder.Binder
import com.arkivanov.mvikotlin.extensions.coroutines.bind
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.internetcloud.strava.domain.common.mapper.Mapper
import ru.internetcloud.strava.domain.training.mvi.api.TrainingDetailStore
import ru.internetcloud.strava.presentation.training.detail.model.UiTrainingDetailEvent
import ru.internetcloud.strava.presentation.training.detail.model.UiTrainingDetailState

class TrainingDetailViewModel(
    id: Long,
    private val store: TrainingDetailStore,
    private val stateMapper: Mapper<TrainingDetailStore.State, UiTrainingDetailState>,
    private val eventMapper: Mapper<TrainingDetailStore.Event, UiTrainingDetailEvent>,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val initialState =
        savedStateHandle.get<UiTrainingDetailState>(KEY_TRAINING_DETAIL_STATE)
            ?: UiTrainingDetailState.Loading(isChanged = false)

    private val _screenState = MutableStateFlow(initialState)
    val screenState = _screenState.asStateFlow()

    private val screenEventChannel = Channel<UiTrainingDetailEvent>(Channel.BUFFERED)
    val screenEventFlow: Flow<UiTrainingDetailEvent>
        get() = screenEventChannel.receiveAsFlow()

    private val binder: Binder

    private var isChanged: Boolean = false

    init {
        binder = bind(Dispatchers.Main.immediate) {
            store.states.map(stateMapper::map) bindTo (::acceptState)
            store.labels.map(eventMapper::map) bindTo (::acceptEvent)
        }
        binder.start()
        fetchTraining(id = id, isChanged = false)
    }

    fun fetchTraining(id: Long, isChanged: Boolean) {
        this.isChanged = isChanged
        store.accept(TrainingDetailStore.Intent.Load(id))
    }

    fun deleteTraining() = store.accept(TrainingDetailStore.Intent.Delete)

    private fun acceptState(state: UiTrainingDetailState) {
        _screenState.value = when (state) {
            is UiTrainingDetailState.Error -> state.copy(isChanged = this.isChanged)
            is UiTrainingDetailState.Loading -> state.copy(isChanged = this.isChanged)
            is UiTrainingDetailState.Success -> state.copy(isChanged = this.isChanged)
        }
    }

    private fun acceptEvent(event: UiTrainingDetailEvent) {
        viewModelScope.launch {
            screenEventChannel.send(event)
        }
    }

    override fun onCleared() {
        super.onCleared()
        binder.stop()
        store.dispose()
    }

    companion object {
        private const val KEY_TRAINING_DETAIL_STATE = "key_training_detail_state"
    }
}
