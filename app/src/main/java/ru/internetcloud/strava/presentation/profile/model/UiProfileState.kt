package ru.internetcloud.strava.presentation.profile.model

import ru.internetcloud.strava.domain.common.model.Source
import ru.internetcloud.strava.domain.profile.model.Profile

sealed interface UiProfileState {

    data class Success(
        val profile: Profile,
        val source: Source,
        val saving: Boolean = false
    ) : UiProfileState

    data class Error(val exception: Exception) : UiProfileState

    object Loading : UiProfileState
}
