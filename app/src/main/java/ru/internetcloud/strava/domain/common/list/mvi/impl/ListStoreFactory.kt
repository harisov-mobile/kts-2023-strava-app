package ru.internetcloud.strava.domain.common.list.mvi.impl

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kotlinx.coroutines.CoroutineDispatcher
import ru.internetcloud.strava.domain.common.list.mvi.api.ListStore
import ru.internetcloud.strava.domain.common.list.mvi.api.PagingSource
import ru.internetcloud.strava.domain.common.list.mvi.model.Result

class ListStoreFactory<Key, Value, Query>(
    private val storeFactory: StoreFactory,
    private val initialState: ListStore.State<Key, Value, Query>,
    private val remoteSource: PagingSource<Key, Value, Query>,
    private val cachedSource: PagingSource<Key, Value, Query>,
    private val mainContext: CoroutineDispatcher,
    private val ioContext: CoroutineDispatcher
) {

    fun create(): ListStore<Key, Value, Query> {
        return object : ListStore<Key, Value, Query>,
            Store<ListStore.Intent, ListStore.State<Key, Value, Query>, ListStore.Label> by storeFactory.create(
                name = LIST_STORE_NAME,
                initialState = initialState,
                bootstrapper = null,
                executorFactory = ::createExecutor,
                reducer = ListReducer(initialState)
            ) {}
    }

    private fun createExecutor(): Executor<ListStore.Intent, ListStore.Action, ListStore.State<Key, Value, Query>, Result, ListStore.Label> {
        return ListExecutor(
            initialState = initialState,
            remoteSource = remoteSource,
            cachedSource = cachedSource,
            mainContext = mainContext,
            ioContext = ioContext
        )
    }

    companion object {
        private const val LIST_STORE_NAME = "ListStore"
    }
}
