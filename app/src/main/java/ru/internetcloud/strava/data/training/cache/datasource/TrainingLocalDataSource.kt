package ru.internetcloud.strava.data.training.cache.datasource

import ru.internetcloud.strava.data.training.cache.model.TrainingListItemDbModel

interface TrainingLocalDataSource {

    suspend fun getTrainings(): List<TrainingListItemDbModel>
}
