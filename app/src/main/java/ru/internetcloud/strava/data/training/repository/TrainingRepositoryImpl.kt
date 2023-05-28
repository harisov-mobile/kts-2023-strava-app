package ru.internetcloud.strava.data.training.repository

import ru.internetcloud.strava.data.training.network.datasource.TrainingRemoteApiDataSourceImpl
import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.training.TrainingRepository
import ru.internetcloud.strava.domain.training.model.Training
import ru.internetcloud.strava.domain.training.model.TrainingListItem

class TrainingRepositoryImpl : TrainingRepository {

    private val trainingRemoteApiDataSource = TrainingRemoteApiDataSourceImpl()

    override suspend fun getTrainings(page: Int): DataResponse<List<TrainingListItem>> {
        return trainingRemoteApiDataSource.getTrainings(page)
    }

    override suspend fun getTraining(id: Long): DataResponse<Training> {
        return trainingRemoteApiDataSource.getTraining(id)
    }
}
