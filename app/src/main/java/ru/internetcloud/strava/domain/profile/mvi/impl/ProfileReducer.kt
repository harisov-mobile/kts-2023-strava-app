package ru.internetcloud.strava.domain.profile.mvi.impl

import com.arkivanov.mvikotlin.core.store.Reducer
import ru.internetcloud.strava.domain.profile.mvi.api.ProfileStore

internal class ProfileReducer : Reducer<ProfileStore.State, ProfileStoreFactory.Message> {

    override fun ProfileStore.State.reduce(
        msg: ProfileStoreFactory.Message
    ) = when (msg) {
        is ProfileStoreFactory.Message.SetLoading -> ProfileStore.State.Loading

        is ProfileStoreFactory.Message.SetSuccess -> ProfileStore.State.Success(
            profile = msg.profile,
            source = msg.source,
            saving = msg.saving
        )

        is ProfileStoreFactory.Message.SetError -> ProfileStore.State.Error(
            exception = msg.error
        )
    }
}
