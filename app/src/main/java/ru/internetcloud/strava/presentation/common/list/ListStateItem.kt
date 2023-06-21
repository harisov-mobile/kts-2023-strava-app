package ru.internetcloud.strava.presentation.common.list

import androidx.annotation.StringRes

sealed interface ListStateItem {

    data class LoadingPage(val isListLoading: Boolean) : ListStateItem

    data class ErrorPage(
        @StringRes val message: Int,
        val throwable: Throwable
    ) : ListStateItem

    data class ErrorList(
        @StringRes val message: Int,
        val throwable: Throwable
    ) : ListStateItem

    data class EmptyList(
        @StringRes val message: Int
    ) : ListStateItem
}
