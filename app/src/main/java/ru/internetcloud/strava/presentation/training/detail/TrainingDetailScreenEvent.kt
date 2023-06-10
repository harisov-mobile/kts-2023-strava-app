package ru.internetcloud.strava.presentation.training.detail

import ru.internetcloud.strava.presentation.util.StringVs

sealed interface TrainingDetailScreenEvent {

    object NavigateBack : TrainingDetailScreenEvent

    data class ShowMessage(val stringVs: StringVs) : TrainingDetailScreenEvent
}
