package ru.internetcloud.strava.domain.training.usecase

import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.training.TrainingRepository
import ru.internetcloud.strava.domain.training.model.Training

class GetTrainingUseCase(private val trainingRepository: TrainingRepository) {

    suspend fun getTraining(id: Long): DataResponse<Training> {
        return trainingRepository.getTraining(id = id)
    }
}
