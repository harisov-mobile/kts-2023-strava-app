package ru.internetcloud.strava.di

import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import ru.internetcloud.strava.data.firstlaunch.FirstLaunchSharedPreferencesStorage
import ru.internetcloud.strava.data.token.TokenSharedPreferencesStorage

val storageModule = module {

    single {
        FirstLaunchSharedPreferencesStorage(androidApplication())
    }

    single {
        TokenSharedPreferencesStorage(androidApplication())
    }
}
