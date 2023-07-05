package ru.internetcloud.strava.presentation.training.list.model

import ru.internetcloud.strava.domain.common.list.mvi.api.ListStore
import ru.internetcloud.strava.domain.common.mapper.Mapper
import ru.internetcloud.strava.domain.training.model.TrainingListItem

class UiTrainingListStateMapper : Mapper<ListStore.State<Any, TrainingListItem, Any>, UiTrainingListState<TrainingListItem>> {

    override fun map(item: ListStore.State<Any, TrainingListItem, Any>): UiTrainingListState<TrainingListItem> {
        return UiTrainingListState(
            items = item.items,
            loadState = item.loadState,
            source = item.source
        )
    }
}
