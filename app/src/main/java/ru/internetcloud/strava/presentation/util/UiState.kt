package ru.internetcloud.strava.presentation.util

sealed interface UiState<out T> {

    data class Success<out T>(val data: T) : UiState<T>

    data class Error(val exception: Exception) : UiState<Nothing>

    object Loading : UiState<Nothing>

    object EmptyData : UiState<Nothing>
}
