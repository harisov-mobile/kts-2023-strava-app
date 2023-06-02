package ru.internetcloud.strava.data.training.repository

import ru.internetcloud.strava.data.training.cache.datasource.TrainingLocalDataSourceImpl
import ru.internetcloud.strava.data.training.mapper.TrainingListItemMapper
import ru.internetcloud.strava.data.training.mapper.TrainingMapper
import ru.internetcloud.strava.data.training.network.datasource.TrainingRemoteApiDataSourceImpl
import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.common.model.Source
import ru.internetcloud.strava.domain.training.TrainingRepository
import ru.internetcloud.strava.domain.training.model.Training
import ru.internetcloud.strava.domain.training.model.TrainingListItem

class TrainingRepositoryImpl : TrainingRepository {

    private val trainingRemoteApiDataSource = TrainingRemoteApiDataSourceImpl()
    private val trainingLocalDataSource = TrainingLocalDataSourceImpl()

    private val trainingMapper = TrainingMapper()
    private val trainingListItemMapper = TrainingListItemMapper()

    override suspend fun getTrainings(page: Int): DataResponse<List<TrainingListItem>> {
        var result = trainingRemoteApiDataSource.getTrainings(page)

        if (result is DataResponse.Success) {
            trainingLocalDataSource.deleteAllTrainingListItems()
            trainingLocalDataSource.deleteAllTrainings()
            trainingLocalDataSource.insertTrainingListItems(
                trainingListItemMapper.fromListDomainToListDbModel(
                    result.data
                )
            )
        } else {
            val listDbModel = trainingLocalDataSource.getTrainingListItems()
            if (listDbModel.isNotEmpty()) {
                result = DataResponse.Success(
                    data = trainingListItemMapper.fromListDbModelToListDomain(listDbModel),
                    source = Source.LocalCache
                )
            }
        }

        return result
    }

    override suspend fun getTraining(id: Long): DataResponse<Training> {
        var result = trainingRemoteApiDataSource.getTraining(id)

        if (result is DataResponse.Success) {
            trainingLocalDataSource.insertTraining(
                trainingMapper.fromDomainToDbModel(
                    result.data
                )
            )
        } else {
            trainingLocalDataSource.getTraining(id)?.let { trainingDbModel ->
                result = DataResponse.Success(
                    data = trainingMapper.fromDbModelToDomain(trainingDbModel),
                    source = Source.LocalCache
                )
            }
        }

        return result
    }

    override suspend fun deleteTrainingsInLocalCache() {
        trainingLocalDataSource.deleteAllTrainings()
        trainingLocalDataSource.deleteAllTrainingListItems()
    }

    override suspend fun addTraining(training: Training): DataResponse<Training> {
        return trainingRemoteApiDataSource.addTraining(training)
    }

    override suspend fun updateTraining(training: Training): DataResponse<Training> {
        return trainingRemoteApiDataSource.updateTraining(training)
    }
}
