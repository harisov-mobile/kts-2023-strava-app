package ru.internetcloud.strava.domain.common.list.mvi.api

import com.arkivanov.mvikotlin.core.store.Store
import ru.internetcloud.strava.domain.common.list.mvi.model.LoadState
import ru.internetcloud.strava.domain.common.list.mvi.model.LoadType
import ru.internetcloud.strava.domain.common.list.mvi.model.PagingState
import ru.internetcloud.strava.domain.common.model.Source

interface ListStore<Key, Value, Query> : Store<ListStore.Intent, ListStore.State<Key, Value, Query>, ListStore.Label> {

    sealed class Intent {
        data class Reboot<Query>(val query: Query) : Intent()
        data class Retry(val loadType: LoadType = LoadType.NEXT) : Intent()
        data class LoadingPage(val loadType: LoadType = LoadType.NEXT) : Intent()
        object Refresh : Intent()
    }

    data class State<Key, Value, Query>(
        val items: List<Value> = emptyList(),
        val paging: PagingState<Key, Query>,
        val loadState: LoadState = LoadState.ListSuccess,
        val source: Source
    ) {
        val query: Query
            get() = paging.loadParams.query
    }

    sealed class Label {
        data class Error(val throwable: Throwable) : Label()
    }

    sealed interface Action // без этой строки не компилируется, выдает ошибку
}
