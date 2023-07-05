package ru.internetcloud.strava.di

import org.koin.dsl.module
import ru.internetcloud.strava.data.common.database.AppDatabase

val databaseModule = module {

    single {
        AppDatabase.getInstance(application = get()).appDao()
    }
}
