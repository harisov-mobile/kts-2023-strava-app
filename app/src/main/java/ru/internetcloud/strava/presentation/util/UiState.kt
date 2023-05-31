package ru.internetcloud.strava.presentation.util

import ru.internetcloud.strava.domain.common.model.Source

sealed interface UiState<out T> {

    data class Success<out T>(val data: T, val source: Source, val isChanged: Boolean = false) : UiState<T>

    data class Error(val exception: Exception) : UiState<Nothing>

    object Loading : UiState<Nothing>

    object EmptyData : UiState<Nothing>
}
