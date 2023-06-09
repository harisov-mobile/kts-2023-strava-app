package ru.internetcloud.strava.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.internetcloud.strava.presentation.auth.AuthViewModel
import ru.internetcloud.strava.presentation.main.MainScreenViewModel
import ru.internetcloud.strava.presentation.profile.ProfileViewModel
import ru.internetcloud.strava.presentation.start.StartViewModel
import ru.internetcloud.strava.presentation.training.detail.TrainingDetailViewModel
import ru.internetcloud.strava.presentation.training.edit.TrainingEditViewModel
import ru.internetcloud.strava.presentation.training.list.TrainingListViewModel

val viewModelModule = module {

    viewModel {
        TrainingListViewModel(
            getProfileUseCase = get(),
            listStore = get(named("TrainingListStore")),
            stateMapper = get(named("UiTrainingListStateMapper")),
            savedStateHandle = get()
        )
    }

    viewModel { params ->
        TrainingEditViewModel(
            id = params.get(),
            editMode = params.get(),
            getTrainingUseCase = get(),
            updateTrainingUseCase = get(),
            addTrainingUseCase = get(),
            dateConverter = get(),
            savedStateHandle = get()
        )
    }

    viewModel { params ->
        TrainingDetailViewModel(
            id = params.get(),
            store = get(named("TrainingDetailStore")),
            stateMapper = get(named("UiTrainingDetailStateMapper")),
            eventMapper = get(named("UiTrainingDetailEventMapper")),
            savedStateHandle = get()
        )
    }

    viewModel {
        StartViewModel(
            getFirstLaunchUseCase = get(),
            setFirstLaunchUseCase = get(),
            authUseCase = get()
        )
    }

    viewModel {
        ProfileViewModel(
            store = get(named("ProfileStore")),
            stateMapper = get(named("UiProfileStateMapper")),
            savedStateHandle = get()
        )
    }

    viewModel { params ->
        MainScreenViewModel(
            app = get(),
            keyMessage = params.get(),
            getInternetStatusUseCase = get(),
            logoutUseCase = get(),
            unauthorizedHandler = get(),
            logoutClickHelper = get()
        )
    }

    viewModel {
        AuthViewModel(
            authRepository = get(),
            authService = get(),
            deleteProfileInLocalCacheUseCase = get(),
            deleteTrainingsInLocalCacheUseCase = get()
        )
    }
}
