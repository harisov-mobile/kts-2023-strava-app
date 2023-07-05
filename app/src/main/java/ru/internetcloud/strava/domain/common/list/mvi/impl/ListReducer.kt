package ru.internetcloud.strava.domain.common.list.mvi.impl

import com.arkivanov.mvikotlin.core.store.Reducer
import ru.internetcloud.strava.domain.common.list.mvi.api.ListStore
import ru.internetcloud.strava.domain.common.list.mvi.model.LoadState
import ru.internetcloud.strava.domain.common.list.mvi.model.LoadType
import ru.internetcloud.strava.domain.common.list.mvi.model.PagingState
import ru.internetcloud.strava.domain.common.list.mvi.model.Result

class ListReducer<Key, Value, Query>(
    private val initialState: ListStore.State<Key, Value, Query>
) : Reducer<ListStore.State<Key, Value, Query>, Result> {

    @Suppress("UNCHECKED_CAST")
    private val PagingState.LoadResult.Page<*, *>.asPage: PagingState.LoadResult.Page<Key, Value>
        get() = this as PagingState.LoadResult.Page<Key, Value>

    @Suppress("UNCHECKED_CAST")
    private val List<*>.asItems: List<Value>
        get() = this as List<Value>

    @Suppress("UNCHECKED_CAST")
    private val Any?.asQuery: Query
        get() = this as Query

    override fun ListStore.State<Key, Value, Query>.reduce(result: Result) = when (result) {
        is Result.Loaded<*, *, *> -> {
            val page = result.page.asPage
            initialState.loaded(
                query = result.query.asQuery,
                loadState = result.loadState,
                page = page
            )
        }
        is Result.PageLoaded<*, *> -> pageLoaded(
            items = result.items.asItems,
            page = result.page.asPage,
            loadType = result.loadType
        )
        is Result.LoadingState -> copy(
            loadState = result.loadState
        )
    }

    private fun ListStore.State<Key, Value, Query>.loaded(
        page: PagingState.LoadResult.Page<Key, Value>,
        loadState: LoadState,
        query: Query
    ): ListStore.State<Key, Value, Query> {
        return copy(
            items = page.data,
            loadState = loadState,
            paging = paging.getPagingState(page.data.size, query, page),
            source = page.source
        )
    }

    private fun ListStore.State<Key, Value, Query>.pageLoaded(
        items: List<Value>,
        page: PagingState.LoadResult.Page<Key, Value>,
        loadType: LoadType
    ): ListStore.State<Key, Value, Query> {
        return copy(
            items = items,
            loadState = LoadState.PageSuccess(loadType),
            paging = paging.getPagingState(items.size, query, page),
            source = page.source
        )
    }

    private fun PagingState<Key, Query>.getPagingState(
        loadSize: Int,
        query: Query,
        page: PagingState.LoadResult.Page<Key, Value>
    ): PagingState<Key, Query> {
        return copy(
            loadParams = PagingState.LoadParams(
                loadSize = loadSize,
                query = query.asQuery,
                key = page.key
            ),
            isLastPage = page.isLastPage
        )
    }
}
