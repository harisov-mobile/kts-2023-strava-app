package ru.internetcloud.strava.presentation.training.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkivanov.mvikotlin.core.binder.Binder
import com.arkivanov.mvikotlin.extensions.coroutines.bind
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.internetcloud.strava.domain.common.list.mvi.api.ListStore
import ru.internetcloud.strava.domain.common.list.mvi.model.LoadState
import ru.internetcloud.strava.domain.common.mapper.Mapper
import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.profile.model.Profile
import ru.internetcloud.strava.domain.profile.usecase.GetProfileUseCase
import ru.internetcloud.strava.domain.training.model.TrainingListItem
import ru.internetcloud.strava.presentation.training.list.model.UiTrainingListState

class TrainingListViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val listStore: ListStore<Int, TrainingListItem, Unit>,
    private val stateMapper: Mapper<ListStore.State<Int, TrainingListItem, Unit>, UiTrainingListState<TrainingListItem>>,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val initialState =
        savedStateHandle.get(KEY_TRAINING_LIST_STATE) ?: UiTrainingListState<TrainingListItem>()

    private val _screenState = MutableStateFlow(initialState)
    val screenState = _screenState.asStateFlow()

//    private val _isRefreshing = MutableStateFlow(false)
//    val isRefreshing = _isRefreshing.asStateFlow()

    private val binder: Binder

    private var profile: Profile? = null

    init {
        binder = bind(Dispatchers.Main.immediate) {
            listStore.states.map(stateMapper::map) bindTo (::acceptState)
        }
        binder.start()
        onReboot()
    }

    private fun acceptState(state: UiTrainingListState<TrainingListItem>) {
        _screenState.value = state.copy(profile = profile)
    }

    fun onRetry() {
        if (profileAvailable()) {
            listStore.accept(ListStore.Intent.Retry())
        }
    }

    fun onReboot() {
        viewModelScope.launch {
            val profileIsLoaded = if (profile == null) {
                _screenState.value = UiTrainingListState(loadState = LoadState.ListLoading)
                when (val profileDataResponse = getProfileUseCase.getProfile()) {
                    is DataResponse.Success -> {
                        profile = profileDataResponse.data
                        true
                    }

                    is DataResponse.Error -> {
                        _screenState.value = UiTrainingListState(
                            loadState = LoadState.ListError(throwable = profileDataResponse.exception)
                        )
                        false
                    }
                }
            } else {
                true
            }

            if (profileIsLoaded) {
                listStore.accept(ListStore.Intent.Reboot(Unit))
            }
        }
    }

    fun onRefresh() {
        if (profileAvailable()) {
            listStore.accept(ListStore.Intent.Refresh)
        }
    }

    fun onLoadNextPage() {
        if (profileAvailable()) {
            listStore.accept(ListStore.Intent.LoadingPage())
        }
    }

    fun profileAvailable(): Boolean {
        return if (profile == null) {
            _screenState.value = UiTrainingListState(
                loadState = LoadState.ListError(throwable = IllegalStateException("profile is unavailable"))
            )
            false
        } else {
            true
        }
    }

    override fun onCleared() {
        super.onCleared()
        binder.stop()
        listStore.dispose()
    }

    companion object {
        private const val KEY_TRAINING_LIST_STATE = "key_training_list_state"
    }
}
