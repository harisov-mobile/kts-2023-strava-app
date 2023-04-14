package ru.internetcloud.strava

import android.app.Application
import ru.internetcloud.strava.data.network.api.StravaApiFactory
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        StravaApiFactory.init(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
