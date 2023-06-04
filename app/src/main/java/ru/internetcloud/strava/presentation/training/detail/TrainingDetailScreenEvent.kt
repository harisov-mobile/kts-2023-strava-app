package ru.internetcloud.strava.presentation.training.detail

sealed interface TrainingDetailScreenEvent {

    object NavigateBack : TrainingDetailScreenEvent

    data class ShowMessage(val message: String) : TrainingDetailScreenEvent
}
