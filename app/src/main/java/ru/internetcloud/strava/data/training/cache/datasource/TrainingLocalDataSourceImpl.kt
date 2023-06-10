package ru.internetcloud.strava.data.training.cache.datasource

import ru.internetcloud.strava.data.common.database.AppDatabaseHolder
import ru.internetcloud.strava.data.training.cache.model.LocalTraining
import ru.internetcloud.strava.data.training.cache.model.LocalTrainingListItem

class TrainingLocalDataSourceImpl : TrainingLocalDataSource {

    private val appDao = AppDatabaseHolder.appDao // потом будет dependency injecton вместо этой переменной

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
