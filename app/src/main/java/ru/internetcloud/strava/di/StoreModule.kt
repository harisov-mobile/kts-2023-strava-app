package ru.internetcloud.strava.di

import android.util.Log
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.logger.Logger
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import org.koin.dsl.module
import ru.internetcloud.strava.domain.profile.mvi.api.ProfileStore
import ru.internetcloud.strava.domain.profile.mvi.impl.ProfileStoreFactory

val storeModule = module {

    factory<StoreFactory> {
        val logger = object : Logger {
            override fun log(text: String) {
                Log.d("MVI", text)
            }
        }
        LoggingStoreFactory(DefaultStoreFactory(), logger = logger)
    }

    factory<ProfileStore> {
        ProfileStoreFactory(
            storeFactory = get(),
            getProfileUseCase = get()
        ).create()
    }
}
