package ru.internetcloud.strava.presentation.training.edit

sealed interface TrainingEditScreenEvent {

    data class ShowMessage(val message: String) : TrainingEditScreenEvent

    object NavigateBackWithRefresh : TrainingEditScreenEvent
}
