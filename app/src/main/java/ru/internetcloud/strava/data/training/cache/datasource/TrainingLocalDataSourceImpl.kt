package ru.internetcloud.strava.data.training.cache.datasource

import ru.internetcloud.strava.data.common.database.AppDatabaseHolder
import ru.internetcloud.strava.data.training.cache.model.TrainingListItemDbModel

class TrainingLocalDataSourceImpl : TrainingLocalDataSource {

    private val appDao = AppDatabaseHolder.appDao // потом будет dependency injecton вместо этой переменной

    override suspend fun getTrainingListItems(): List<TrainingListItemDbModel> {
        return appDao.getTrainingListItems()
    }

    override suspend fun insertTrainingListItems(list: List<TrainingListItemDbModel>) {
        appDao.insertTrainingListItems(list)
    }

    override suspend fun deleteAllTrainingListItems() {
        appDao.deleteAllTrainingListItems()
    }
}
