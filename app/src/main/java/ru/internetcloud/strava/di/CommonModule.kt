package ru.internetcloud.strava.di

import net.openid.appauth.AuthorizationService
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import ru.internetcloud.strava.data.auth.network.AppAuth
import ru.internetcloud.strava.data.auth.network.AppAuthImpl
import ru.internetcloud.strava.data.common.ErrorMessageConverter
import ru.internetcloud.strava.data.common.ErrorMessageConverterImpl
import ru.internetcloud.strava.domain.common.util.DateConverter
import ru.internetcloud.strava.domain.common.util.DateConverterImpl
import ru.internetcloud.strava.domain.token.UnauthorizedHandler
import ru.internetcloud.strava.domain.token.UnauthorizedHandlerImpl
import ru.internetcloud.strava.presentation.logout.LogoutClickHelper
import ru.internetcloud.strava.presentation.logout.LogoutClickHelperImpl

val commonModule = module {

    factory {
        AuthorizationService(androidApplication())
    }

    single<ErrorMessageConverter> {
        ErrorMessageConverterImpl(androidApplication())
    }

    single<AppAuth> {
        AppAuthImpl()
    }

    single<UnauthorizedHandler> {
        UnauthorizedHandlerImpl()
    }

    single<LogoutClickHelper> {
        LogoutClickHelperImpl()
    }

    single<DateConverter> {
        DateConverterImpl()
    }
}
