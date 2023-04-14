package ru.internetcloud.strava.domain.usecase

import ru.internetcloud.strava.domain.TrainingRepository
import ru.internetcloud.strava.domain.model.DataResponse
import ru.internetcloud.strava.domain.model.Training

class GetTrainingUseCase(private val trainingRepository: TrainingRepository) {

    suspend fun getTraining(id: Long): DataResponse<Training> {
        return trainingRepository.getTraining(id = id)
    }
}
