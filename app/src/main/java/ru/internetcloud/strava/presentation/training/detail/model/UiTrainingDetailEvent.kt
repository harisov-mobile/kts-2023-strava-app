package ru.internetcloud.strava.presentation.training.detail.model

import ru.internetcloud.strava.domain.common.util.StringVs

sealed interface UiTrainingDetailEvent {

    object NavigateBack : UiTrainingDetailEvent

    data class ShowMessage(val stringVs: StringVs) : UiTrainingDetailEvent
}
