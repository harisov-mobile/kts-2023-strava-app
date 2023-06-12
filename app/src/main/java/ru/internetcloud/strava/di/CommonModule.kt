package ru.internetcloud.strava.di

import net.openid.appauth.AuthorizationService
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import ru.internetcloud.strava.data.auth.network.AppAuth
import ru.internetcloud.strava.data.common.ErrorMessageConverter
import ru.internetcloud.strava.domain.common.model.SportTypeKeeper
import ru.internetcloud.strava.domain.common.util.DateConverter
import ru.internetcloud.strava.domain.token.UnauthorizedHandler
import ru.internetcloud.strava.presentation.logout.LogoutClickHelper

val commonModule = module {

    factory {
        AuthorizationService(androidApplication())
    }

    single {
        ErrorMessageConverter(androidApplication())
    }

    single {
        AppAuth()
    }

    single {
        UnauthorizedHandler()
    }

    single {
        LogoutClickHelper()
    }

    single {
        DateConverter()
    }

    single {
        SportTypeKeeper()
    }
}
