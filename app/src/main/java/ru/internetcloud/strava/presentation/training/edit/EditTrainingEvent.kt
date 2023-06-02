package ru.internetcloud.strava.presentation.training.edit

sealed interface EditTrainingEvent {

    data class OnNameChange(val name: String) : EditTrainingEvent

    data class OnDescriptionChange(val description: String) : EditTrainingEvent

    data class OnSportTypeChange(val sportType: String) : EditTrainingEvent

    data class OnStartDateChange(val year: Int, val month: Int, val day: Int) : EditTrainingEvent

    data class OnStartTimeChange(val hour: Int, val minute: Int) : EditTrainingEvent

    data class OnDurationChange(val durationTime: Int) : EditTrainingEvent

    data class OnDistanceChange(val distance: Float) : EditTrainingEvent
}
