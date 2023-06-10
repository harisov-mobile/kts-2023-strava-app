package ru.internetcloud.strava.data.training.cache.datasource

import ru.internetcloud.strava.data.training.cache.model.LocalTraining
import ru.internetcloud.strava.data.training.cache.model.LocalTrainingListItem

interface TrainingLocalDataSource {

    suspend fun getTrainingListItems(): List<LocalTrainingListItem>

    suspend fun insertTrainingListItems(list: List<LocalTrainingListItem>)

    suspend fun deleteAllTrainingListItems()

    suspend fun getTraining(id: Long): LocalTraining?

    suspend fun insertTraining(localTraining: LocalTraining)

    suspend fun deleteAllTrainings()
}
