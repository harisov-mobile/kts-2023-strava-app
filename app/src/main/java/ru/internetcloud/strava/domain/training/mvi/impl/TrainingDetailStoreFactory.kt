package ru.internetcloud.strava.domain.training.mvi.impl

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import ru.internetcloud.strava.domain.common.model.Source
import ru.internetcloud.strava.domain.profile.ProfileRepository
import ru.internetcloud.strava.domain.profile.model.ProfileWithTraining
import ru.internetcloud.strava.domain.training.TrainingRepository
import ru.internetcloud.strava.domain.training.mvi.api.TrainingDetailStore

internal class TrainingDetailStoreFactory(
    private val storeFactory: StoreFactory,
    private val profileRepository: ProfileRepository,
    private val trainingRepository: TrainingRepository
) {

    fun create(): TrainingDetailStore = object :
        TrainingDetailStore,
        Store<TrainingDetailStore.Intent, TrainingDetailStore.State, TrainingDetailStore.Event> by storeFactory.create(
            name = TrainingDetailStore::class.simpleName,
            initialState = TrainingDetailStore.State.Loading,
            bootstrapper = null,
            executorFactory = {
                TrainingDetailExecutor(
                    profileRepository = profileRepository,
                    trainingRepository = trainingRepository
                )
            },
            reducer = TrainingDetailReducer()
        ) {}

    sealed interface Message {
        object SetLoading : Message
        data class SetSuccess(
            val profileWithTraining: ProfileWithTraining,
            val source: Source
        ) : Message

        data class SetError(
            val error: Exception
        ) : Message
    }
}
