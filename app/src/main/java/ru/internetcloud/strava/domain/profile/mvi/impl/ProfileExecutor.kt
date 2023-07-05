package ru.internetcloud.strava.domain.profile.mvi.impl

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.profile.mvi.api.ProfileStore
import ru.internetcloud.strava.domain.profile.usecase.GetProfileUseCase

internal class ProfileExecutor(
    private val getProfileUseCase: GetProfileUseCase
) : CoroutineExecutor<ProfileStore.Intent, ProfileStore.Action, ProfileStore.State, ProfileStoreFactory.Message, ProfileStore.Event>() {

    override fun executeIntent(intent: ProfileStore.Intent, getState: () -> ProfileStore.State) {
        scope.launch {
            when (intent) {
                ProfileStore.Intent.Load -> loadProfile()
            }
        }
    }

    private suspend fun loadProfile() {
        dispatch(ProfileStoreFactory.Message.SetLoading)

        when (val dataResponse = getProfileUseCase.getProfile()) {
            is DataResponse.Success -> {
                dispatch(
                    ProfileStoreFactory.Message.SetSuccess(
                        profile = dataResponse.data,
                        source = dataResponse.source
                    )
                )
            }

            is DataResponse.Error -> {
                dispatch(ProfileStoreFactory.Message.SetError(dataResponse.exception))
            }
        }
    }
}
