package ru.internetcloud.strava.domain.training.mvi.impl

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import ru.internetcloud.strava.R
import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.common.util.toStringVs
import ru.internetcloud.strava.domain.profile.ProfileRepository
import ru.internetcloud.strava.domain.profile.model.Profile
import ru.internetcloud.strava.domain.profile.model.ProfileWithTraining
import ru.internetcloud.strava.domain.training.TrainingRepository
import ru.internetcloud.strava.domain.training.mvi.api.TrainingDetailStore

internal class TrainingDetailExecutor(
    private val profileRepository: ProfileRepository,
    private val trainingRepository: TrainingRepository
) : CoroutineExecutor<TrainingDetailStore.Intent, TrainingDetailStore.Action, TrainingDetailStore.State, TrainingDetailStoreFactory.Message, TrainingDetailStore.Event>() {

    override fun executeIntent(intent: TrainingDetailStore.Intent, getState: () -> TrainingDetailStore.State) {
        scope.launch {
            when (intent) {
                is TrainingDetailStore.Intent.Load -> loadTraining(trainingId = intent.trainingId)

                is TrainingDetailStore.Intent.Delete -> deleteTraining(state = getState())
            }
        }
    }

    private suspend fun loadTraining(trainingId: Long) {
        dispatch(TrainingDetailStoreFactory.Message.SetLoading)

        when (val profileDataResponse = profileRepository.getProfile()) {
            is DataResponse.Success -> {
                val profile = profileDataResponse.data
                loadProfileWithTraining(profile = profile, trainingId = trainingId)
            }

            is DataResponse.Error -> {
                dispatch(
                    TrainingDetailStoreFactory.Message.SetError(
                        error = profileDataResponse.exception
                    )
                )
            }
        }
    }

    private suspend fun loadProfileWithTraining(profile: Profile, trainingId: Long) {
        when (val trainingDataResponse = trainingRepository.getTraining(id = trainingId)) {
            is DataResponse.Success -> {
                val profileWithTraining = ProfileWithTraining(
                    profile = profile,
                    training = trainingDataResponse.data
                )
                dispatch(
                    TrainingDetailStoreFactory.Message.SetSuccess(
                        profileWithTraining = profileWithTraining,
                        source = trainingDataResponse.source
                    )
                )
            }

            is DataResponse.Error -> {
                dispatch(
                    TrainingDetailStoreFactory.Message.SetError(
                        error = trainingDataResponse.exception
                    )
                )
            }
        }
    }

    private suspend fun deleteTraining(state: TrainingDetailStore.State) {
        if (state is TrainingDetailStore.State.Success) {
            when (val deleteDataResponse = trainingRepository.deleteTraining(
                state.profileWithTraining.training.id
            )) {
                is DataResponse.Success -> {
                    publish(TrainingDetailStore.Event.ShowMessage(R.string.training_deleted.toStringVs()))
                    publish(TrainingDetailStore.Event.NavigateBack)
                }

                is DataResponse.Error -> {
                    publish(
                        TrainingDetailStore.Event.ShowMessage(
                            R.string.training_can_not_delete_training_with_arg.toStringVs(
                                deleteDataResponse.exception.message.toString()
                            )
                        )
                    )
                }
            }
        } else {
            publish(TrainingDetailStore.Event.ShowMessage(R.string.training_can_not_delete_training.toStringVs()))
        }
    }
}
