package ru.internetcloud.strava.domain.common.list.mvi.model

sealed class Result {
    data class LoadingState(val loadState: LoadState) : Result()

    data class PageLoaded<Key, Value>(
        val items: List<Value>,
        val page: PagingState.LoadResult.Page<Key, Value>,
        val loadType: LoadType
    ) : Result()

    data class Loaded<Key, Value, Query>(
        val query: Query,
        val page: PagingState.LoadResult.Page<Key, Value>,
        val loadState: LoadState
    ) : Result()
}
