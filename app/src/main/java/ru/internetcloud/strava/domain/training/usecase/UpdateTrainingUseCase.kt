package ru.internetcloud.strava.domain.training.usecase

import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.training.TrainingRepository
import ru.internetcloud.strava.domain.training.model.Training

class UpdateTrainingUseCase(private val trainingRepository: TrainingRepository) {

    suspend fun updateTraining(training: Training): DataResponse<Training> {
        return trainingRepository.updateTraining(training)
    }
}
