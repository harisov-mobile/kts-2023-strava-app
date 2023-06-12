package ru.internetcloud.strava.di

import org.koin.dsl.module
import ru.internetcloud.strava.data.auth.network.AuthRepository
import ru.internetcloud.strava.data.auth.network.AuthRepositoryImpl
import ru.internetcloud.strava.data.firstlaunch.repository.FirstLaunchRepositoryImpl
import ru.internetcloud.strava.data.internet.repository.InternetStatusRepositoryImpl
import ru.internetcloud.strava.data.logout.repository.LogoutRepositoryImpl
import ru.internetcloud.strava.data.profile.repository.ProfileRepositoryImpl
import ru.internetcloud.strava.data.token.repository.TokenRepositoryImpl
import ru.internetcloud.strava.data.training.repository.TrainingRepositoryImpl
import ru.internetcloud.strava.domain.firstlaunch.FirstLaunchRepository
import ru.internetcloud.strava.domain.internet.InternetStatusRepository
import ru.internetcloud.strava.domain.logout.LogoutRepository
import ru.internetcloud.strava.domain.profile.ProfileRepository
import ru.internetcloud.strava.domain.token.TokenRepository
import ru.internetcloud.strava.domain.training.TrainingRepository

val repositoryModule = module {

    single<ProfileRepository> {
        ProfileRepositoryImpl(
            profileRemoteApiDataSource = get(),
            profileLocalDataSource = get(),
            profileMapper = get()
        )
    }

    single<TrainingRepository> {
        TrainingRepositoryImpl(
            trainingRemoteApiDataSource = get(),
            trainingLocalDataSource = get(),
            trainingMapper = get(),
            trainingListItemMapper = get()
        )
    }

    single<TokenRepository> {
        TokenRepositoryImpl(
            tokenSharedPreferencesStorage = get()
        )
    }

    single<FirstLaunchRepository> {
        FirstLaunchRepositoryImpl(
            firstLaunchSharedPreferencesStorage = get()
        )
    }

    single<InternetStatusRepository> {
        InternetStatusRepositoryImpl()
    }

    single<LogoutRepository> {
        LogoutRepositoryImpl(
            logoutApi = get(),
            logoutMapper = get(),
            tokenRepository = get(),
            errorMessageConverter = get()
        )
    }

    single<AuthRepository> {
        AuthRepositoryImpl(
            tokenRepository = get(),
            appAuth = get()
        )
    }
}
