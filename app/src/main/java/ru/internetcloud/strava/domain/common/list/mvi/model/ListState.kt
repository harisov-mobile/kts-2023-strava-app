package ru.internetcloud.strava.domain.common.list.mvi.model

data class ListState<T>(
    val items: List<T>,
    val isLastPage: Boolean
)
