package ru.internetcloud.strava.domain.profile.mvi.api

import com.arkivanov.mvikotlin.core.store.Store
import ru.internetcloud.strava.domain.common.model.Source
import ru.internetcloud.strava.domain.profile.model.Profile

interface ProfileStore : Store<ProfileStore.Intent, ProfileStore.State, ProfileStore.Event> {

    sealed interface State {
        data class Success(
            val profile: Profile,
            val source: Source,
            val saving: Boolean = false) : State

        data class Error(val exception: Exception) : State

        object Loading : State
    }

    sealed interface Intent {
        object Load : Intent
        data class Save(val profile: Profile, val source: Source) : Intent
        data class Change(val profile: Profile, val source: Source) : Intent
    }

    sealed interface Event

    sealed interface Action // без этой строки не компилируется, выдает ошибку
}
