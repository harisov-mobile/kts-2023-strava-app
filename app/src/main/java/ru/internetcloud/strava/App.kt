package ru.internetcloud.strava

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.internetcloud.strava.di.commonModule
import ru.internetcloud.strava.di.dataSourceModule
import ru.internetcloud.strava.di.databaseModule
import ru.internetcloud.strava.di.mapperModule
import ru.internetcloud.strava.di.networkModule
import ru.internetcloud.strava.di.repositoryModule
import ru.internetcloud.strava.di.storageModule
import ru.internetcloud.strava.di.storeModule
import ru.internetcloud.strava.di.useCaseModule
import ru.internetcloud.strava.di.viewModelModule
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        initDi()
    }

    private fun initDi() {
        startKoin {
            androidContext(this@App)
            modules(
                mapperModule,
                storageModule,
                databaseModule,
                networkModule,
                dataSourceModule,
                repositoryModule,
                useCaseModule,
                storeModule,
                viewModelModule,
                commonModule
            )
        }
    }
}
