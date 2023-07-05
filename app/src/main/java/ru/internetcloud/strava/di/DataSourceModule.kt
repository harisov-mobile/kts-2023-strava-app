package ru.internetcloud.strava.di

import org.koin.dsl.module
import ru.internetcloud.strava.data.profile.cache.datasource.ProfileLocalDataSource
import ru.internetcloud.strava.data.profile.cache.datasource.ProfileLocalDataSourceImpl
import ru.internetcloud.strava.data.profile.network.datasource.ProfileRemoteApiDataSource
import ru.internetcloud.strava.data.profile.network.datasource.ProfileRemoteApiDataSourceImpl
import ru.internetcloud.strava.data.training.cache.datasource.TrainingLocalDataSource
import ru.internetcloud.strava.data.training.cache.datasource.TrainingLocalDataSourceImpl
import ru.internetcloud.strava.data.training.network.datasource.TrainingRemoteApiDataSource
import ru.internetcloud.strava.data.training.network.datasource.TrainingRemoteApiDataSourceImpl

val dataSourceModule = module {

    single<ProfileLocalDataSource> {
        ProfileLocalDataSourceImpl(appDao = get())
    }

    single<ProfileRemoteApiDataSource> {
        ProfileRemoteApiDataSourceImpl(
            profileApi = get(),
            profileMapper = get(),
            errorMessageConverter = get()
        )
    }

    single<TrainingLocalDataSource> {
        TrainingLocalDataSourceImpl(appDao = get())
    }

    single<TrainingRemoteApiDataSource> {
        TrainingRemoteApiDataSourceImpl(
            trainingApi = get(),
            trainingMapper = get(),
            trainingListItemMapper = get(),
            errorMessageConverter = get()
        )
    }
}
