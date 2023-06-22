package ru.internetcloud.strava.domain.training.usecase

import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.training.TrainingRepository
import ru.internetcloud.strava.domain.training.model.Training

class AddTrainingUseCase(private val trainingRepository: TrainingRepository) {

    suspend fun addTraining(training: Training): DataResponse<Training> {
        return trainingRepository.addTraining(training)
    }
}
