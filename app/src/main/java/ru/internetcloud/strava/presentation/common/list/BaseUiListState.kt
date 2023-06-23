package ru.internetcloud.strava.presentation.common.list

import androidx.annotation.StringRes
import ru.internetcloud.strava.domain.common.list.mvi.model.LoadState

abstract class BaseUiListState<Value> {
    abstract val items: List<Value>
    abstract val loadState: LoadState
}

fun <T : Any> BaseUiListState<T>.itemsWithState(
    errorList: ListStateItem.ErrorList,
    emptyList: ListStateItem.EmptyList,
    errorPage: ListStateItem.ErrorPage
): List<Any> = when {
    items.isEmpty() && isRefreshLoading() || isListLoading() -> listOf(
        ListStateItem.LoadingPage(isListLoading = true)
    )

    items.isNotEmpty() && isPageLoading() -> items.asAny().toMutableList().apply {
        add(
            ListStateItem.LoadingPage(
                isListLoading = false
            )
        )
    }.toList()

    items.isNotEmpty() && isPageError() -> items.asAny().toMutableList()
        .apply {
            add(
                errorPage.copy(
                    message = errorPage.message,
                    throwable = (this@itemsWithState.loadState as LoadState.PageError).throwable
                )
            )
        }.toList()

    items.isEmpty() && isRefreshError() ->
        listOf(
            errorList.copy(
                message = errorList.message,
                throwable = (this.loadState as LoadState.RefreshError).throwable
            )
        )

    isListError() ->
        listOf(
            errorList.copy(
                message = errorList.message,
                throwable = (this.loadState as LoadState.ListError).throwable
            )
        )

    items.isEmpty() -> listOf(emptyList)
    else -> items
}

fun <T> BaseUiListState<T>.loadNextPage(
    index: Int,
    onLoadNextPage: () -> Unit
) {
    if (index >= items.size - 1 && !isLoading()) {
        onLoadNextPage()
    }
}

fun getEmptyList(
    query: String,
    itemsIsEmpty: Boolean,
    @StringRes emptyListMessage: Int,
    @StringRes searchEmptyListMessage: Int,
    @StringRes searchEmptyButtonMessage: Int
): ListStateItem.EmptyList {
    fun getEmptyListMessage() = when (query.isNotEmpty()) {
        true -> searchEmptyListMessage
        false -> emptyListMessage
    }

    fun getEmptyListButtonText() = when (query.isNotEmpty()) {
        true -> searchEmptyButtonMessage
        false -> emptyListMessage
    }

    val emptyList = ListStateItem.EmptyList(
        message = emptyListMessage
    )

    return when (itemsIsEmpty) {
        true -> when (query.isEmpty()) {
            true -> emptyList
            else -> ListStateItem.EmptyList(
                message = getEmptyListMessage()
            )
        }

        else -> emptyList
    }
}

fun <T> BaseUiListState<T>.isSwipeEnabled() =
    isRefreshLoading().not() && isListLoading().not()

fun <T> BaseUiListState<T>.isLoading(): Boolean = loadState is LoadState.ListLoading ||
        loadState is LoadState.PageLoading

fun <T> BaseUiListState<T>.isListLoading(): Boolean = loadState is LoadState.ListLoading

fun <T> BaseUiListState<T>.isPageLoading(): Boolean = loadState is LoadState.PageLoading

fun <T> BaseUiListState<T>.isPageError(): Boolean = loadState is LoadState.PageError

fun <T> BaseUiListState<T>.isListSuccess(): Boolean = loadState is LoadState.ListSuccess

fun <T> BaseUiListState<T>.isListError(): Boolean = loadState is LoadState.ListError

fun <T> BaseUiListState<T>.isRefreshLoading(): Boolean = loadState is LoadState.RefreshLoading

fun <T> BaseUiListState<T>.isRefreshSuccess(): Boolean = loadState is LoadState.RefreshSuccess

fun <T> BaseUiListState<T>.isRefreshError(): Boolean = loadState is LoadState.RefreshError

fun <T : Any> List<T>.asAny() = this as List<Any>
