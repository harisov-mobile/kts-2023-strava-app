package ru.internetcloud.strava.presentation.training.detail.model

import ru.internetcloud.strava.domain.common.model.Source
import ru.internetcloud.strava.domain.profile.model.ProfileWithTraining

sealed interface UiTrainingDetailState {

    data class Success(
        val profileWithTraining: ProfileWithTraining,
        val source: Source,
        val isChanged: Boolean = false
    ) : UiTrainingDetailState

    data class Error(
        val exception: Exception,
        val isChanged: Boolean = false
    ) : UiTrainingDetailState

    data class Loading(
        val isChanged: Boolean = false
    ) : UiTrainingDetailState
}
