package ru.internetcloud.strava.domain.training.mvi.impl

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import ru.internetcloud.strava.domain.common.model.Source
import ru.internetcloud.strava.domain.profile.model.ProfileWithTraining
import ru.internetcloud.strava.domain.profile.usecase.GetProfileUseCase
import ru.internetcloud.strava.domain.training.mvi.api.TrainingDetailStore
import ru.internetcloud.strava.domain.training.usecase.DeleteTrainingUseCase
import ru.internetcloud.strava.domain.training.usecase.GetTrainingUseCase

internal class TrainingDetailStoreFactory(
    private val storeFactory: StoreFactory,
    private val getProfileUseCase: GetProfileUseCase,
    private val getTrainingUseCase: GetTrainingUseCase,
    private val deleteTrainingUseCase: DeleteTrainingUseCase
) {

    fun create(): TrainingDetailStore = object :
        TrainingDetailStore,
        Store<TrainingDetailStore.Intent, TrainingDetailStore.State, TrainingDetailStore.Event> by storeFactory.create(
            name = TrainingDetailStore::class.simpleName,
            initialState = TrainingDetailStore.State.Loading,
            bootstrapper = null,
            executorFactory = {
                TrainingDetailExecutor(
                    getProfileUseCase = getProfileUseCase,
                    getTrainingUseCase = getTrainingUseCase,
                    deleteTrainingUseCase = deleteTrainingUseCase
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
