package ru.internetcloud.strava.domain.common.model

sealed interface DataResponse<out T> {

    data class Success<out T>(val data: T, val source: Source) : DataResponse<T>

    data class Error(val exception: Exception) : DataResponse<Nothing>
}
