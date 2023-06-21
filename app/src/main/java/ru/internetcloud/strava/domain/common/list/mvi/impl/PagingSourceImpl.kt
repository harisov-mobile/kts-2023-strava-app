package ru.internetcloud.strava.domain.common.list.mvi.impl

import ru.internetcloud.strava.domain.common.list.mvi.api.PagingSource
import ru.internetcloud.strava.domain.common.list.mvi.model.ListLoadParams
import ru.internetcloud.strava.domain.common.list.mvi.model.ListState
import ru.internetcloud.strava.domain.common.list.mvi.model.PagingState
import ru.internetcloud.strava.domain.common.model.DataResponse

class PagingSourceImpl<Value, Query>(
    private val loadData: suspend (ListLoadParams<Query>) -> DataResponse<ListState<Value>>
) : PagingSource<Int, Value, Query> {

    override suspend fun load(
        loadParams: PagingState.LoadParams<Int, Query>
    ): PagingState.LoadResult<Int, Value> {
        val key = loadParams.key ?: 1
        return makeLoadResult {
            val response = loadData(
                ListLoadParams(
                    page = key,
                    pageSize = PAGE_SIZE,
                    query = loadParams.query
                )
            )
            when (response) {
                is DataResponse.Success -> PagingState.LoadResult.Page(
                    data = response.data.items,
                    key = key.plus(1),
                    isLastPage = response.data.isLastPage,
                    source = response.source
                )
                is DataResponse.Error -> PagingState.LoadResult.Error(response.exception)
            }
        }
    }

    companion object {
        private const val PAGE_SIZE = 10
    }
}

inline fun <Key, Value> makeLoadResult(
    tryBlock: () -> PagingState.LoadResult<Key, Value>
): PagingState.LoadResult<Key, Value> {
    return try {
        tryBlock()
    } catch (t: Throwable) {
        PagingState.LoadResult.Error(t)
    }
}
