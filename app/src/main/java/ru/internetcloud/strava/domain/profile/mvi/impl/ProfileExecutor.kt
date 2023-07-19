package ru.internetcloud.strava.domain.profile.mvi.impl

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.common.model.Source
import ru.internetcloud.strava.domain.profile.ProfileRepository
import ru.internetcloud.strava.domain.profile.model.Profile
import ru.internetcloud.strava.domain.profile.mvi.api.ProfileStore

internal class ProfileExecutor(
    private val profileRepository: ProfileRepository
) : CoroutineExecutor<ProfileStore.Intent, ProfileStore.Action, ProfileStore.State, ProfileStoreFactory.Message, ProfileStore.Event>() {

    override fun executeIntent(intent: ProfileStore.Intent, getState: () -> ProfileStore.State) {
        scope.launch {
            when (intent) {
                ProfileStore.Intent.Load -> loadProfile()
                is ProfileStore.Intent.Change -> onChange(intent.profile, intent.source)
                is ProfileStore.Intent.Save -> saveProfile(intent.profile, intent.source)
            }
        }
    }

    private suspend fun saveProfile(profile: Profile, source: Source) {
        dispatch(
            ProfileStoreFactory.Message.SetSuccess(
                profile = profile,
                source = source,
                saving = true
            )
        )

        when (val dataResponse = profileRepository.saveWeight(profile.weight)) {
            is DataResponse.Success -> {
                dispatch(
                    ProfileStoreFactory.Message.SetSuccess(
                        profile = dataResponse.data,
                        source = dataResponse.source,
                        saving = false
                    )
                )
            }

            is DataResponse.Error -> {
                dispatch(ProfileStoreFactory.Message.SetError(dataResponse.exception))
            }
        }
    }

    private fun onChange(profile: Profile, source: Source) {
        dispatch(
            ProfileStoreFactory.Message.SetSuccess(
                profile = profile,
                source = source
            )
        )
    }

    private suspend fun loadProfile() {
        dispatch(ProfileStoreFactory.Message.SetLoading)

        when (val dataResponse = profileRepository.getProfile()) {
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
