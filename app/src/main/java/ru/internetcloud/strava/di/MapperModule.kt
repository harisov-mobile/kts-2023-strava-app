package ru.internetcloud.strava.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.internetcloud.strava.data.logout.mapper.LogoutMapper
import ru.internetcloud.strava.data.profile.mapper.ProfileMapper
import ru.internetcloud.strava.data.training.mapper.TrainingListItemMapper
import ru.internetcloud.strava.data.training.mapper.TrainingMapper
import ru.internetcloud.strava.domain.common.mapper.Mapper
import ru.internetcloud.strava.domain.profile.mvi.api.ProfileStore
import ru.internetcloud.strava.presentation.profile.model.UiProfileState
import ru.internetcloud.strava.presentation.profile.model.UiProfileStateMapper

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
}
