package ru.internetcloud.strava.domain.training.mvi.api

import com.arkivanov.mvikotlin.core.store.Store
import ru.internetcloud.strava.domain.common.model.Source
import ru.internetcloud.strava.domain.common.util.StringVs
import ru.internetcloud.strava.domain.profile.model.ProfileWithTraining

interface TrainingDetailStore : Store<TrainingDetailStore.Intent, TrainingDetailStore.State, TrainingDetailStore.Event> {

    sealed interface State {
        data class Success(
            val profileWithTraining: ProfileWithTraining,
            val source: Source
        ) : State

        data class Error(
            val exception: Exception
        ) : State

        object Loading : State
    }

    sealed interface Intent {
        data class Load(val trainingId: Long) : Intent
        object Delete : Intent
    }

    sealed interface Event {
        data class ShowMessage(val stringVs: StringVs) : Event
        object NavigateBack : Event
    }

    sealed interface Action // без этой строки не компилируется, выдает ошибку
}
