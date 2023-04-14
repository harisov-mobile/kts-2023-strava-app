package ru.internetcloud.strava.domain.model

sealed interface DataResponse<out T> {

    data class Success<out T>(val data: T) : DataResponse<T>

    data class Error(val exception: Exception) : DataResponse<Nothing>
}
