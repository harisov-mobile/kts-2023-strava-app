package ru.internetcloud.strava.presentation.common.list

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

private typealias DefaultItemListKey<T> = ((item: T) -> Any)
private typealias KeyResult = ((index: Int, item: Any) -> Any)

inline fun <reified T> LazyListScope.itemsWithListStates(
    items: List<Any>,
    noinline key: ((item: T) -> Any)? = null,
    emptyItemButtonVisible: Boolean = true,
    noinline onErrorRetry: () -> Unit = {},
    noinline onErrorRetryPage: () -> Unit = {},
    noinline onEmptyRetry: () -> Unit = {},
    noinline loadListContent: @Composable LazyItemScope.(index: Int) -> Unit,
    noinline itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit
) {
    itemsIndexed(
        items = items,
        key = defaultKey(key)
    ) { index, item ->
        WithListStates<T>(
            item = item,
            emptyItemButtonVisible = emptyItemButtonVisible,
            onErrorRetry = onErrorRetry,
            onEmptyRetry = onEmptyRetry,
            onErrorRetryPage = onErrorRetryPage,
            loadListContent = loadListContent,
            itemContent = { itemContent(index, it) }
        )
    }
}

inline fun <reified T> defaultKey(
    noinline defaultItemListKey: DefaultItemListKey<T>? = null
): KeyResult? {
    defaultItemListKey ?: return null

    return { _, item ->
        when (item) {
            is T -> defaultItemListKey(item as T)
            is ListStateItem -> item::class.java.name
            else -> error("Unknown item type: ${item::class.java.name}")
        }
    }
}

@Composable
inline fun <reified T> LazyItemScope.WithListStates(
    item: Any,
    emptyItemButtonVisible: Boolean = false,
    noinline onErrorRetry: () -> Unit = {},
    noinline onEmptyRetry: () -> Unit = {},
    noinline onErrorRetryPage: () -> Unit = {},
    noinline loadListContent: @Composable LazyItemScope.(index: Int) -> Unit,
    noinline itemContent: @Composable LazyItemScope.(item: T) -> Unit
) {
    when (item) {
        is T -> itemContent(item)
        is ListStateItem.LoadingPage -> {
            when (item.isListLoading) {
                true -> loadListContent(0)
                false -> ItemLoadingPage()
            }
        }
        is ListStateItem.ErrorList -> ErrorLoadingDataBox(
            boxModifier = Modifier
                .fillParentMaxSize(),
            item = item,
            onRetry = onErrorRetry
        )
        is ListStateItem.EmptyList -> ItemEmptyList(
            item = item,
            isRefreshButtonVisible = emptyItemButtonVisible,
            onRefreshClick = onEmptyRetry
        )
        is ListStateItem.ErrorPage -> ItemErrorPage(
            item = item,
            onRetry = onErrorRetryPage
        )
    }
}
