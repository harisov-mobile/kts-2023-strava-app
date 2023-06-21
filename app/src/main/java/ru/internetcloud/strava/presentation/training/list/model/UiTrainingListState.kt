package ru.internetcloud.strava.presentation.training.list.model

import ru.internetcloud.strava.R
import ru.internetcloud.strava.domain.common.list.mvi.model.LoadState
import ru.internetcloud.strava.domain.common.model.Source
import ru.internetcloud.strava.domain.profile.model.Profile
import ru.internetcloud.strava.domain.training.model.TrainingListItem
import ru.internetcloud.strava.presentation.common.list.BaseUiListState
import ru.internetcloud.strava.presentation.common.list.ListStateItem
import ru.internetcloud.strava.presentation.common.list.itemsWithState

data class UiTrainingListState<Value>(
    override val items: List<Value> = listOf(),
    override val loadState: LoadState = LoadState.ListLoading,
    val source: Source = Source.RemoteApi,
    val profile: Profile? = null
) : BaseUiListState<Value>()

internal fun UiTrainingListState<TrainingListItem>.trainingListItemsWithState(): List<Any> {
    return itemsWithState(
        errorList = errorList,
        emptyList = emptyList,
        errorPage = errorPage
    )
}

private val errorList = ListStateItem.ErrorList(
    message = R.string.strava_server_unavailable,
    throwable = Throwable()
)

private val emptyList = ListStateItem.EmptyList(
    message = R.string.training_list_is_empty
)

private val errorPage = ListStateItem.ErrorPage(
    message = R.string.strava_server_unavailable,
    throwable = Throwable()
)
