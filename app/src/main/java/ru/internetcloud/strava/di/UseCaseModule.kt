package ru.internetcloud.strava.di

import org.koin.dsl.module
import ru.internetcloud.strava.domain.firstlaunch.usecase.GetFirstLaunchUseCase
import ru.internetcloud.strava.domain.firstlaunch.usecase.SetFirstLaunchUseCase
import ru.internetcloud.strava.domain.internet.usecase.GetInternetStatusUseCase
import ru.internetcloud.strava.domain.logout.usecase.LogoutUseCase
import ru.internetcloud.strava.domain.profile.usecase.DeleteProfileInLocalCacheUseCase
import ru.internetcloud.strava.domain.profile.usecase.GetProfileUseCase
import ru.internetcloud.strava.domain.token.usecase.AuthUseCase
import ru.internetcloud.strava.domain.training.usecase.AddTrainingUseCase
import ru.internetcloud.strava.domain.training.usecase.DeleteTrainingUseCase
import ru.internetcloud.strava.domain.training.usecase.DeleteTrainingsInLocalCacheUseCase
import ru.internetcloud.strava.domain.training.usecase.GetTrainingUseCase
import ru.internetcloud.strava.domain.training.usecase.UpdateTrainingUseCase

val useCaseModule = module {

    factory {
        GetTrainingUseCase(trainingRepository = get())
    }

    factory {
        GetProfileUseCase(profileRepository = get())
    }

    factory {
        UpdateTrainingUseCase(trainingRepository = get())
    }

    factory {
        AddTrainingUseCase(trainingRepository = get())
    }

    factory {
        GetInternetStatusUseCase(internetStatusRepository = get())
    }

    factory {
        LogoutUseCase(logoutRepository = get())
    }

    factory {
        DeleteProfileInLocalCacheUseCase(profileRepository = get())
    }

    factory {
        DeleteTrainingsInLocalCacheUseCase(trainingRepository = get())
    }

    factory {
        GetFirstLaunchUseCase(firstLaunchRepository = get())
    }

    factory {
        SetFirstLaunchUseCase(firstLaunchRepository = get())
    }

    factory {
        DeleteTrainingUseCase(trainingRepository = get())
    }

    factory {
        AuthUseCase(tokenRepository = get())
    }
}
