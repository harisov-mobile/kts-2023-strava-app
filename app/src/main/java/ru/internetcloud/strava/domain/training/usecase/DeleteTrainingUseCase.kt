package ru.internetcloud.strava.domain.training.usecase

import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.training.TrainingRepository

class DeleteTrainingUseCase(private val trainingRepository: TrainingRepository) {

    suspend fun deleteTraining(id: Long): DataResponse<Long> {
        return trainingRepository.deleteTraining(id)
    }
}
