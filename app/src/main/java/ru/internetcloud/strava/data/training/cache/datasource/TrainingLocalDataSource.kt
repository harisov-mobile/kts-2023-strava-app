package ru.internetcloud.strava.data.training.cache.datasource

import ru.internetcloud.strava.data.training.cache.model.TrainingDbModel
import ru.internetcloud.strava.data.training.cache.model.TrainingListItemDbModel

interface TrainingLocalDataSource {

    suspend fun getTrainingListItems(): List<TrainingListItemDbModel>

    suspend fun insertTrainingListItems(list: List<TrainingListItemDbModel>)

    suspend fun deleteAllTrainingListItems()

    suspend fun getTraining(id: Long): TrainingDbModel?

    suspend fun insertTraining(trainingDbModel: TrainingDbModel)

    suspend fun deleteAllTrainings()
}
