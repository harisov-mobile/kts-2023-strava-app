package ru.internetcloud.strava.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.internetcloud.strava.data.logout.mapper.LogoutMapper
import ru.internetcloud.strava.data.profile.mapper.ProfileMapper
import ru.internetcloud.strava.data.training.mapper.TrainingListItemMapper
import ru.internetcloud.strava.data.training.mapper.TrainingMapper
import ru.internetcloud.strava.domain.common.list.mvi.api.ListStore
import ru.internetcloud.strava.domain.common.mapper.Mapper
import ru.internetcloud.strava.domain.profile.mvi.api.ProfileStore
import ru.internetcloud.strava.domain.training.model.TrainingListItem
import ru.internetcloud.strava.domain.training.mvi.api.TrainingDetailStore
import ru.internetcloud.strava.presentation.profile.model.UiProfileState
import ru.internetcloud.strava.presentation.profile.model.UiProfileStateMapper
import ru.internetcloud.strava.presentation.training.detail.model.UiTrainingDetailEvent
import ru.internetcloud.strava.presentation.training.detail.model.UiTrainingDetailEventMapper
import ru.internetcloud.strava.presentation.training.detail.model.UiTrainingDetailState
import ru.internetcloud.strava.presentation.training.detail.model.UiTrainingDetailStateMapper
import ru.internetcloud.strava.presentation.training.list.model.UiTrainingListState
import ru.internetcloud.strava.presentation.training.list.model.UiTrainingListStateMapper

val mapperModule = module {

    factory {
        ProfileMapper()
    }

    factory {
        TrainingMapper(
            dateConverter = get()
        )
    }

    factory {
        TrainingListItemMapper(
            dateConverter = get()
        )
    }

    factory {
        LogoutMapper()
    }

    factory<Mapper<ProfileStore.State, UiProfileState>>(named("UiProfileStateMapper")) {
        UiProfileStateMapper()
    }

    factory<Mapper<TrainingDetailStore.State, UiTrainingDetailState>>(named("UiTrainingDetailStateMapper")) {
        UiTrainingDetailStateMapper()
    }

    factory<Mapper<TrainingDetailStore.Event, UiTrainingDetailEvent>>(named("UiTrainingDetailEventMapper")) {
        UiTrainingDetailEventMapper()
    }

    factory<Mapper<ListStore.State<Any, TrainingListItem, Any>, UiTrainingListState<TrainingListItem>>>(
        named("UiTrainingListStateMapper")
    ) {
        UiTrainingListStateMapper()
    }
}
