package ru.internetcloud.strava.data.training.cache.datasource

import ru.internetcloud.strava.data.common.database.AppDao
import ru.internetcloud.strava.data.training.cache.model.LocalTraining
import ru.internetcloud.strava.data.training.cache.model.LocalTrainingListItem

class TrainingLocalDataSourceImpl(
    private val appDao: AppDao
) : TrainingLocalDataSource {
    override suspend fun getTrainingListItems(): List<LocalTrainingListItem> {
        return appDao.getTrainingListItems()
    }

    override suspend fun insertTrainingListItems(list: List<LocalTrainingListItem>) {
        appDao.insertTrainingListItems(list)
    }

    override suspend fun deleteAllTrainingListItems() {
        appDao.deleteAllTrainingListItems()
    }

    override suspend fun getTraining(id: Long): LocalTraining? {
        return appDao.getTraining(id)
    }

    override suspend fun insertTraining(localTraining: LocalTraining) {
        appDao.insertTraining(localTraining)
    }

    override suspend fun deleteAllTrainings() {
        appDao.deleteAllTrainings()
    }
}
