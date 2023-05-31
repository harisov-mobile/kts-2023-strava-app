package ru.internetcloud.strava.presentation.training.edit

import java.util.Date

sealed interface EditTrainingEvent {

    data class OnNameChange(val name: String) : EditTrainingEvent

    data class OnDescriptionChange(val description: String) : EditTrainingEvent

    data class OnSportTypeChange(val sportType: String) : EditTrainingEvent

    data class OnStartDateChange(val startDate: Date) : EditTrainingEvent

    data class OnDurationChange(val durationTime: Int) : EditTrainingEvent

    data class OnDistanceChange(val distance: Float) : EditTrainingEvent
}
