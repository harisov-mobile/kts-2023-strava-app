package ru.internetcloud.strava.domain.training.mvi.impl

import com.arkivanov.mvikotlin.core.store.Reducer
import ru.internetcloud.strava.domain.training.mvi.api.TrainingDetailStore

internal class TrainingDetailReducer : Reducer<TrainingDetailStore.State, TrainingDetailStoreFactory.Message> {

    override fun TrainingDetailStore.State.reduce(
        msg: TrainingDetailStoreFactory.Message
    ) = when (msg) {
        is TrainingDetailStoreFactory.Message.SetLoading -> TrainingDetailStore.State.Loading

        is TrainingDetailStoreFactory.Message.SetSuccess -> TrainingDetailStore.State.Success(
            profileWithTraining = msg.profileWithTraining,
            source = msg.source
        )

        is TrainingDetailStoreFactory.Message.SetError -> TrainingDetailStore.State.Error(
            exception = msg.error
        )
    }
}
