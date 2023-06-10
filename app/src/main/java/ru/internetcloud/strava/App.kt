package ru.internetcloud.strava

import android.app.Application
import ru.internetcloud.strava.data.common.ErrorMessageConverter
import ru.internetcloud.strava.data.common.StravaApiFactory
import ru.internetcloud.strava.data.common.database.AppDatabaseHolder
import ru.internetcloud.strava.data.firstlaunch.FirstLaunchSharedPreferencesStorage
import ru.internetcloud.strava.data.token.TokenSharedPreferencesStorage
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        StravaApiFactory.init(this)
        ErrorMessageConverter.init(this)
        FirstLaunchSharedPreferencesStorage.init(this)
        TokenSharedPreferencesStorage.init(this)
        AppDatabaseHolder.init(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
