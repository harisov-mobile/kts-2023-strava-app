package ru.internetcloud.strava.presentation.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.arkivanov.mvikotlin.core.binder.Binder
import com.arkivanov.mvikotlin.extensions.coroutines.bind
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import ru.internetcloud.strava.domain.common.mapper.Mapper
import ru.internetcloud.strava.domain.common.model.Source
import ru.internetcloud.strava.domain.profile.model.Profile
import ru.internetcloud.strava.domain.profile.mvi.api.ProfileStore
import ru.internetcloud.strava.presentation.profile.model.UiProfileState

class ProfileViewModel(
    private val store: ProfileStore,
    private val stateMapper: Mapper<ProfileStore.State, UiProfileState>,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val initialState = savedStateHandle.get<UiProfileState>(KEY_PROFILE_STATE) ?: UiProfileState.Loading

    private val _screenState = MutableStateFlow(initialState)
    val screenState = _screenState.asStateFlow()

    private val binder: Binder

    init {
        binder = bind(Dispatchers.Main.immediate) {
            store.states.map(stateMapper::map) bindTo (::acceptState)
        }
        binder.start()
        fetchProfile()
    }

    fun fetchProfile() = store.accept(ProfileStore.Intent.Load)

    private fun acceptState(state: UiProfileState) {
        _screenState.value = state
    }

    fun handleEvent(editProfileEvent: EditProfileEvent) {
        val state = _screenState.value
        if (state is UiProfileState.Success && state.source == Source.RemoteApi) {
            val oldProfile = state.profile
            when (editProfileEvent) {
                is EditProfileEvent.OnWeightChange -> {
                    setScreenState(
                        profile = oldProfile.copy(weight = editProfileEvent.weight),
                        source = state.source
                    )
                }
            }
        }
    }

    private fun setScreenState(profile: Profile, source: Source) {
        val state = _screenState.value
        if (state is UiProfileState.Success) {
            _screenState.value = UiProfileState.Success(
                profile = profile,
                source = source,
                saving = false
            )
            store.accept(
                ProfileStore.Intent.Change(
                    profile = profile,
                    source = source
                )
            )
        }
    }

    fun saveProfile() {
        val state = _screenState.value
        if (state is UiProfileState.Success &&
            state.source == Source.RemoteApi
        ) {
            store.accept(
                ProfileStore.Intent.Save(
                    profile = state.profile,
                    source = state.source
                )
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        binder.stop()
        store.dispose()
    }

    companion object {
        private const val KEY_PROFILE_STATE = "key_profile_state"
    }
}
