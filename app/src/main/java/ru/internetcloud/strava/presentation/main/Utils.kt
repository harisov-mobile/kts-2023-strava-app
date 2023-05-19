package ru.internetcloud.strava.presentation.main

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import ru.internetcloud.strava.presentation.main.composable.LoadingData

private typealias DefaultItemListKey<T> = ((item: T) -> Any)
private typealias KeyResult = ((index: Int, item: Any) -> Any)

inline fun <reified T> LazyListScope.itemsWithListState(
    items: ImmutableList<Any>,
    noinline key: ((item: T) -> Any)? = null,
    noinline contentType: ((item: T) -> Any)? = null,
    noinline itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit
) {
    itemsIndexed(
        items = items,
        key = defaultKey(key),
        contentType = defaultContentType(contentType)
    ) { index, item ->
        when (item) {
            is ListStateItem.LoadingPage -> LoadingData(fillParentMaxSize = item.isListLoading)
            is T -> itemContent(index, item)
        }
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

inline fun <reified T> defaultContentType(
    noinline defaultItemListType: ((item: T) -> Any)? = null
): (index: Int, item: Any) -> Any? {
    defaultItemListType ?: return { _, _ -> null }

    return { _, item ->
        when (item) {
            is T -> defaultItemListType(item)
            is ListStateItem -> item::class.java.name
            else -> error("Unknown item type: ${item::class.java.name}")
        }
    }
}

sealed interface ListStateItem {
    data class LoadingPage(val isListLoading: Boolean) : ListStateItem
}

data class UiListState<Value>(
    val items: PersistentList<Value> = persistentListOf(),
    val isListLoading: Boolean = true,
    val isPageLoading: Boolean = false
)

fun <T : Any> UiListState<T>.itemsWithState(): PersistentList<Any> = when {
    isListLoading -> persistentListOf(ListStateItem.LoadingPage(isListLoading = true))
    isPageLoading -> items.asAny().add(ListStateItem.LoadingPage(isListLoading = false))
    else -> items
}

fun <T : Any> PersistentList<T>.asAny() = this as PersistentList<Any>

fun <T> UiListState<T>.loadNextPage(
    index: Int,
    onLoadNextPage: () -> Unit
) {
    if (index >= items.size - 1 && !(isListLoading || isPageLoading)) {
        onLoadNextPage()
    }
}
