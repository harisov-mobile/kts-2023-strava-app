package ru.internetcloud.strava.presentation.profile

sealed interface EditProfileEvent {
    data class OnWeightChange(val weight: Float) : EditProfileEvent
}
