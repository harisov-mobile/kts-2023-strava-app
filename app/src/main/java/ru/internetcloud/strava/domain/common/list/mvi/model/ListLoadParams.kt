package ru.internetcloud.strava.domain.common.list.mvi.model

data class ListLoadParams<Query>(
    val page: Int,
    val pageSize: Int,
    val query: Query
)
