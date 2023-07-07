package ru.internetcloud.strava.domain.profile.mvi.impl

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import ru.internetcloud.strava.domain.common.model.Source
import ru.internetcloud.strava.domain.profile.ProfileRepository
import ru.internetcloud.strava.domain.profile.model.Profile
import ru.internetcloud.strava.domain.profile.mvi.api.ProfileStore

internal class ProfileStoreFactory(
    private val storeFactory: StoreFactory,
    private val profileRepository: ProfileRepository
) {

    fun create(): ProfileStore = object :
        ProfileStore,
        Store<ProfileStore.Intent, ProfileStore.State, ProfileStore.Event> by storeFactory.create(
            name = ProfileStore::class.simpleName,
            initialState = ProfileStore.State.Loading,
            bootstrapper = null,
            executorFactory = {
                ProfileExecutor(
                    profileRepository = profileRepository
                )
            },
            reducer = ProfileReducer()
        ) {}

    sealed interface Message {
        object SetLoading : Message
        data class SetSuccess(
            val profile: Profile,
            val source: Source,
            val saving: Boolean = false) : Message
        data class SetError(val error: Exception) : Message
    }
}
