package ru.internetcloud.strava.domain.usecase

import ru.internetcloud.strava.domain.TrainingRepository
import ru.internetcloud.strava.domain.model.DataResponse
import ru.internetcloud.strava.domain.model.TrainingListItem

class GetTrainingsUseCase(private val trainingRepository: TrainingRepository) {

    suspend fun getTrainings(page: Int): DataResponse<List<TrainingListItem>> {
        return trainingRepository.getTrainings(page = page)
    }
}
