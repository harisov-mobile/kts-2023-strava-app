package ru.internetcloud.strava.presentation.training.detail.model

import ru.internetcloud.strava.domain.common.mapper.Mapper
import ru.internetcloud.strava.domain.training.mvi.api.TrainingDetailStore

class UiTrainingDetailEventMapper : Mapper<TrainingDetailStore.Event, UiTrainingDetailEvent> {
    override fun map(item: TrainingDetailStore.Event): UiTrainingDetailEvent {
        return when (item) {
            is TrainingDetailStore.Event.ShowMessage -> UiTrainingDetailEvent.ShowMessage(item.stringVs)
            is TrainingDetailStore.Event.NavigateBack -> UiTrainingDetailEvent.NavigateBack
        }
    }
}
