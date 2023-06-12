package ru.internetcloud.strava.di

import org.koin.dsl.module
import ru.internetcloud.strava.data.logout.mapper.LogoutMapper
import ru.internetcloud.strava.data.profile.mapper.ProfileMapper
import ru.internetcloud.strava.data.training.mapper.TrainingListItemMapper
import ru.internetcloud.strava.data.training.mapper.TrainingMapper

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
}
