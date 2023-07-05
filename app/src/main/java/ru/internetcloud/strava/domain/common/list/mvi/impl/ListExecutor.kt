package ru.internetcloud.strava.domain.common.list.mvi.impl

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.internetcloud.strava.domain.common.list.mvi.api.ListStore
import ru.internetcloud.strava.domain.common.list.mvi.api.PagingSource
import ru.internetcloud.strava.domain.common.list.mvi.model.LoadState
import ru.internetcloud.strava.domain.common.list.mvi.model.LoadType
import ru.internetcloud.strava.domain.common.list.mvi.model.PagingState
import ru.internetcloud.strava.domain.common.list.mvi.model.Result

internal class ListExecutor<Key, Value, Query>(
    private val initialState: ListStore.State<Key, Value, Query>,
    private val remoteSource: PagingSource<Key, Value, Query>,
    private val cachedSource: PagingSource<Key, Value, Query>,
    private val ioContext: CoroutineDispatcher,
    mainContext: CoroutineDispatcher
) : CoroutineExecutor<ListStore.Intent, ListStore.Action, ListStore.State<Key, Value, Query>, Result, ListStore.Label>
    (mainContext = mainContext) {

    @Suppress("UNCHECKED_CAST")
    private val Any?.asQuery: Query
        get() = this as Query

    override fun executeIntent(intent: ListStore.Intent, getState: () -> ListStore.State<Key, Value, Query>) {
        scope.launch {
            when (intent) {
                is ListStore.Intent.Reboot<*> -> reboot(query = intent.query.asQuery)

                is ListStore.Intent.Refresh -> refresh(query = getState().query, currentState = getState())

                is ListStore.Intent.Retry -> retry(
                    loadType = intent.loadType,
                    state = getState()
                )

                is ListStore.Intent.LoadingPage -> loadMore(
                    loadType = intent.loadType,
                    state = getState()
                )
            }
        }
    }

    private suspend fun reboot(query: Query) {
        load(
            state = initialState.updateQuery(query),
            loadData = { state -> cachedSource.load(state.paging.loadParams) },
            getLoadStateLoading = { LoadState.ListLoading },
            getLoadStateError = { throwable -> LoadState.ListError(throwable) },
            getLoadResult = { page ->
                Result.Loaded(
                    query = query,
                    page = page,
                    loadState = LoadState.ListSuccess
                )
            }
        )
    }

    private suspend fun refresh(query: Query, currentState: ListStore.State<Key, Value, Query>) {
        if (currentState.loadState is LoadState.ListError) return
        load(
            state = initialState.updateQuery(query),
            loadData = { state -> cachedSource.load(state.paging.loadParams) },
            getLoadStateLoading = { LoadState.RefreshLoading },
            getLoadStateError = { throwable -> LoadState.ListError(throwable) },
            getLoadResult = { page ->
                Result.Loaded(
                    query = query,
                    page = page,
                    loadState = LoadState.RefreshSuccess
                )
            }
        )
    }

    private suspend fun retry(loadType: LoadType, state: ListStore.State<Key, Value, Query>) {
        when (state.loadState) {
            is LoadState.ListError -> reboot(state.query)
            is LoadState.PageError -> {
                val params = state.loadParamsWithLoadType(loadType)
                load(
                    state = state,
                    loadData = { remoteSource.load(params) },
                    getLoadStateLoading = { LoadState.PageLoading(loadType) },
                    getLoadStateError = { LoadState.PageError(it, loadType) },
                    getLoadResult = { page -> getResultPageLoaded(loadType, state, page) }
                )
            }

            else -> return
        }
    }

    private suspend fun loadMore(loadType: LoadType, state: ListStore.State<Key, Value, Query>) {
        if (isLoadMoreValid(state).not()) return

        val params = state.loadParamsWithLoadType(loadType)

        load(
            state = state,
            loadData = { remoteSource.load(params) },
            getLoadStateLoading = { LoadState.PageLoading(loadType) },
            getLoadStateError = { LoadState.PageError(it, loadType) },
            getLoadResult = { page -> getResultPageLoaded(loadType, state, page) }
        )
    }

    private suspend fun load(
        state: ListStore.State<Key, Value, Query>,
        loadData: suspend (ListStore.State<Key, Value, Query>) -> PagingState.LoadResult<Key, Value>,
        getLoadStateLoading: () -> LoadState,
        getLoadStateError: (Throwable) -> LoadState,
        getLoadResult: (PagingState.LoadResult.Page<Key, Value>) -> Result
    ) {
        dispatch(Result.LoadingState(loadState = getLoadStateLoading()))

        val page = withContext(ioContext) { loadData(state) }

        when (page) {
            is PagingState.LoadResult.Error -> {
                dispatch(Result.LoadingState(loadState = getLoadStateError(page.throwable)))
                publish(ListStore.Label.Error(throwable = page.throwable))
            }

            is PagingState.LoadResult.Page -> {
                dispatch(getLoadResult(page))
            }
        }
    }

    private fun getResultPageLoaded(
        loadType: LoadType,
        state: ListStore.State<Key, Value, Query>,
        page: PagingState.LoadResult.Page<Key, Value>
    ): Result.PageLoaded<Key, Value> {
        val items = when (loadType) {
            LoadType.PREV -> page.data.plus(state.items).distinct()
            LoadType.NEXT -> state.items.plus(page.data).distinct()
        }

        return Result.PageLoaded(items, page, loadType)
    }

    private fun isLoadMoreValid(state: ListStore.State<Key, Value, Query>): Boolean = with(state) {
        val isLoading = loadState is LoadState.PageLoading ||
                loadState is LoadState.ListLoading

        val isLoadingError = loadState is LoadState.PageError ||
                loadState is LoadState.ListError

        val isLastPage = state.paging.isLastPage

        return !(isLoading || isLoadingError || items.isEmpty() || isLastPage)
    }

    private fun ListStore.State<Key, Value, Query>.updateQuery(query: Query): ListStore.State<Key, Value, Query> {
        val loadParams = paging.loadParams
        return copy(
            paging = PagingState(
                loadParams = PagingState.LoadParams(
                    loadSize = loadParams.loadSize,
                    query = query,
                    key = loadParams.key
                )
            )
        )
    }

    private fun ListStore.State<Key, Value, Query>.loadParamsWithLoadType(
        loadType: LoadType
    ): PagingState.LoadParams<Key, Query> {
        return paging.loadParams.copy(
            loadType = loadType
        )
    }
}
