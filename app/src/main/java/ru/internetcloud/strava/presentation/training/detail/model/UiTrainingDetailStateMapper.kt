package ru.internetcloud.strava.presentation.training.detail.model

import ru.internetcloud.strava.domain.common.mapper.Mapper
import ru.internetcloud.strava.domain.training.mvi.api.TrainingDetailStore

class UiTrainingDetailStateMapper : Mapper<TrainingDetailStore.State, UiTrainingDetailState> {

    override fun map(item: TrainingDetailStore.State): UiTrainingDetailState {
        return when (item) {
            is TrainingDetailStore.State.Loading -> UiTrainingDetailState.Loading()

            is TrainingDetailStore.State.Success -> UiTrainingDetailState.Success(
                profileWithTraining = item.profileWithTraining,
                source = item.source
            )

            is TrainingDetailStore.State.Error -> UiTrainingDetailState.Error(
                exception = item.exception
            )
        }
    }
}
