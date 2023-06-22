package ru.internetcloud.strava.presentation.training.edit

sealed interface EditMode {

    object Add : EditMode

    object Edit : EditMode
}
