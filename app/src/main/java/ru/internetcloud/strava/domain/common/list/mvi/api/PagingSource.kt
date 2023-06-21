package ru.internetcloud.strava.domain.common.list.mvi.api

import ru.internetcloud.strava.domain.common.list.mvi.model.PagingState

interface PagingSource<Key, Value, Query> {

    suspend fun load(loadParams: PagingState.LoadParams<Key, Query>): PagingState.LoadResult<Key, Value>
}
