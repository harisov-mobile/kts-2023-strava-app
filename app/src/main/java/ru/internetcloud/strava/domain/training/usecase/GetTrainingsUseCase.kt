package ru.internetcloud.strava.domain.training.usecase

import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.training.TrainingRepository
import ru.internetcloud.strava.domain.training.model.TrainingListItem

class GetTrainingsUseCase(private val trainingRepository: TrainingRepository) {

    suspend fun getTrainings(page: Int): DataResponse<List<TrainingListItem>> {
        return trainingRepository.getTrainings(page = page)
    }
}
