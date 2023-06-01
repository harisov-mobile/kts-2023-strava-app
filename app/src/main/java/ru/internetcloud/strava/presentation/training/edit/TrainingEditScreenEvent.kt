package ru.internetcloud.strava.presentation.training.edit

sealed interface TrainingEditScreenEvent {

    data class NavigateToTrainingDetail(val id: Long) : TrainingEditScreenEvent

    data class ShowMessage(val message: String) : TrainingEditScreenEvent
}
