package ru.internetcloud.strava.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.logger.Logger
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.internetcloud.strava.BuildConfig
import ru.internetcloud.strava.domain.common.list.mvi.api.ListStore
import ru.internetcloud.strava.domain.common.list.mvi.impl.ListStoreFactory
import ru.internetcloud.strava.domain.common.list.mvi.impl.PagingSourceImpl
import ru.internetcloud.strava.domain.common.list.mvi.model.LoadState
import ru.internetcloud.strava.domain.common.list.mvi.model.PagingState
import ru.internetcloud.strava.domain.common.model.Source
import ru.internetcloud.strava.domain.profile.mvi.impl.ProfileStoreFactory
import ru.internetcloud.strava.domain.training.TrainingRepository
import ru.internetcloud.strava.domain.training.model.TrainingListItem
import ru.internetcloud.strava.domain.training.mvi.impl.TrainingDetailStoreFactory

val storeModule = module {

    factory<StoreFactory> {
        val logger = object : Logger {
            override fun log(text: String) {
                if (BuildConfig.DEBUG) {
                    // Timber.tag("MVI").d(text)
                }
            }
        }
        LoggingStoreFactory(DefaultStoreFactory(), logger = logger)
    }

    factory(named("ProfileStore")) {
        ProfileStoreFactory(
            storeFactory = get(),
            profileRepository = get()
        ).create()
    }

    factory(named("TrainingDetailStore")) {
        TrainingDetailStoreFactory(
            storeFactory = get(),
            profileRepository = get(),
            trainingRepository = get()
        ).create()
    }

    factory(named("TrainingListStore")) {
        ListStoreFactory<Int, TrainingListItem, Unit>(
            storeFactory = get(),
            initialState = ListStore.State(
                loadState = LoadState.ListLoading,
                paging = PagingState(
                    PagingState.LoadParams(
                        query = Unit
                    )
                ),
                source = Source.RemoteApi
            ),
            mainContext = Dispatchers.Main,
            ioContext = Dispatchers.IO,
            remoteSource = PagingSourceImpl(loadData = get<TrainingRepository>()::getTrainings),
            cachedSource = PagingSourceImpl(loadData = get<TrainingRepository>()::getTrainingsWithLocalCache)
        ).create()
    }
}
