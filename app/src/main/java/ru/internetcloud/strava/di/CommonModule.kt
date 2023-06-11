package ru.internetcloud.strava.di

import net.openid.appauth.AuthorizationService
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val commonModule = module {

    single {
        AuthorizationService(androidApplication())
    }
}
