package ru.internetcloud.strava.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.javafaker.Faker
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.internetcloud.strava.presentation.main.model.ComplexItem
import ru.internetcloud.strava.presentation.main.model.Item
import ru.internetcloud.strava.presentation.main.model.SimpleItem

class MainViewModel : ViewModel() {

    private val mutableListState = MutableStateFlow<UiListState<Item>>(UiListState())
    val listState: StateFlow<UiListState<Item>>
        get() = mutableListState

    init {
        viewModelScope.launch {
            delay(1000)
            mutableListState.value = mutableListState.value.copy(
                items = randomList(20, 0),
                isListLoading = false,
                isPageLoading = false
            )
        }
    }

    private fun randomList(listSize: Int, startId: Int): PersistentList<Item> {
        val faker = Faker.instance()
        return List(listSize) { index ->
            when ((1..2).random()) {
                1 -> SimpleItem(
                    id = startId + index,
                    title = "Пример заголовка ${startId + index}",
                    author = faker.company().name(),
                    likesCount = 0
                )
                2 -> ComplexItem(
                    id = startId + index,
                    title = "Элемент с картинкой",
                    author = faker.name().name(),
                    image = null
                )
                else -> error("Wrong random number")
            }
        }.toPersistentList()
    }

    fun loadNextPage() {
        mutableListState.value = mutableListState.value.copy(
            isListLoading = false,
            isPageLoading = true
        )
        viewModelScope.launch {
            delay(1000)
            val items = mutableListState.value.items
            mutableListState.value = mutableListState.value.copy(
                items = items.addAll(randomList(20, items.size)),
                isListLoading = false,
                isPageLoading = false
            )
        }
    }

    fun increaseLikes(item: Item) {
        val items = mutableListState.value.items
        val foundIndex = mutableListState.value.items.indexOf(item)
        if (foundIndex == NOT_FOUND) {
            throw IllegalStateException("Not found element with id = ${item.id}")
        }
        val oldElement = (items.get(foundIndex) as SimpleItem)
        val newItems = items.set(foundIndex, oldElement.copy(likesCount = oldElement.likesCount + 1))
        mutableListState.value = mutableListState.value.copy(items = newItems)
    }

    companion object {
        private const val NOT_FOUND = -1
    }
}
