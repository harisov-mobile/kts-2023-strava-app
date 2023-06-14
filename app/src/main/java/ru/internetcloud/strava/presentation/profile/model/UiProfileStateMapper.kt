package ru.internetcloud.strava.presentation.profile.model

import ru.internetcloud.strava.domain.common.mapper.Mapper
import ru.internetcloud.strava.domain.profile.mvi.api.ProfileStore

class UiProfileStateMapper : Mapper<ProfileStore.State, UiProfileState> {

    override fun map(item: ProfileStore.State): UiProfileState {
        return when (item) {
            is ProfileStore.State.Loading -> UiProfileState.Loading

            is ProfileStore.State.Success -> UiProfileState.Success(profile = item.profile, source = item.source)

            is ProfileStore.State.Error -> UiProfileState.Error(exception = item.exception)
        }
    }
}
