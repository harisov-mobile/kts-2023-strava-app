package ru.internetcloud.strava.domain.training.usecase

import ru.internetcloud.strava.domain.training.TrainingRepository

class DeleteTrainingsInLocalCacheUseCase(private val trainingRepository: TrainingRepository) {

    suspend fun deleteTrainingsInLocalCache() {
        trainingRepository.deleteTrainingsInLocalCache()
    }
}
