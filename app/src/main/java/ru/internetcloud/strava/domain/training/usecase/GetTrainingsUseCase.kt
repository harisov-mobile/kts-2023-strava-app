package ru.internetcloud.strava.domain.training.usecase

import ru.internetcloud.strava.domain.common.list.mvi.model.ListLoadParams
import ru.internetcloud.strava.domain.common.list.mvi.model.ListState
import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.training.TrainingRepository
import ru.internetcloud.strava.domain.training.model.TrainingListItem

class GetTrainingsUseCase(private val trainingRepository: TrainingRepository) {

    suspend fun getTrainings(params: ListLoadParams<Unit>): DataResponse<ListState<TrainingListItem>> {
        return trainingRepository.getTrainings(params)
    }

    suspend fun getTrainingsWithLocalCache(params: ListLoadParams<Unit>): DataResponse<ListState<TrainingListItem>> {
        return trainingRepository.getTrainingsWithLocalCache(params)
    }
}
