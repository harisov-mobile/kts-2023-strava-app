package ru.internetcloud.strava.domain.common.list.mvi.model

import ru.internetcloud.strava.domain.common.model.Source

data class PagingState<Key, Query>(
    val loadParams: LoadParams<Key, Query>,
    val isLastPage: Boolean = false
) {

    data class LoadParams<Key, Query>(
        val loadSize: Int = DEFAULT_LOAD_SIZE,
        val query: Query,
        val loadType: LoadType = LoadType.NEXT,
        val key: Key? = null
    ) {
        companion object {
            private const val DEFAULT_LOAD_SIZE = 0
        }
    }

    sealed class LoadResult<Key, Value> {

        data class Error<Key, Value>(
            val throwable: Throwable
        ) : LoadResult<Key, Value>()

        data class Page<Key, Value>(
            val data: List<Value> = emptyList(),
            val key: Key? = null,
            val isLastPage: Boolean = false,
            val source: Source
        ) : LoadResult<Key, Value>()
    }
}
