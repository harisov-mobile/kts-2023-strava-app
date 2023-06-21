package ru.internetcloud.strava.data.training.repository

import ru.internetcloud.strava.data.training.cache.datasource.TrainingLocalDataSource
import ru.internetcloud.strava.data.training.mapper.TrainingListItemMapper
import ru.internetcloud.strava.data.training.mapper.TrainingMapper
import ru.internetcloud.strava.data.training.network.datasource.TrainingRemoteApiDataSource
import ru.internetcloud.strava.domain.common.list.mvi.model.ListLoadParams
import ru.internetcloud.strava.domain.common.list.mvi.model.ListState
import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.common.model.Source
import ru.internetcloud.strava.domain.training.TrainingRepository
import ru.internetcloud.strava.domain.training.model.Training
import ru.internetcloud.strava.domain.training.model.TrainingListItem

class TrainingRepositoryImpl(
    private val trainingRemoteApiDataSource: TrainingRemoteApiDataSource,
    private val trainingLocalDataSource: TrainingLocalDataSource,
    private val trainingMapper: TrainingMapper,
    private val trainingListItemMapper: TrainingListItemMapper
) : TrainingRepository {

    override suspend fun getTrainings(params: ListLoadParams<Unit>): DataResponse<ListState<TrainingListItem>> {
        val result = trainingRemoteApiDataSource.getTrainings(params)

        if (result is DataResponse.Success) {
            if (params.page == 1) {
                // при загрузке первой страницы надо очищать полностью данные в БД,
                // иначе удаленная тренировка зависнет в локальном кеше!
                trainingLocalDataSource.deleteAllTrainingListItems()
                trainingLocalDataSource.deleteAllTrainings()
            }
            trainingLocalDataSource.insertTrainingListItems(
                trainingListItemMapper.fromListDomainToListDbModel(
                    result.data.items
                )
            )
        }

        return result
    }

    override suspend fun getTrainingsWithLocalCache(params: ListLoadParams<Unit>): DataResponse<ListState<TrainingListItem>> {
        var result = getTrainings(params)

        if (result is DataResponse.Error) {
            val listDbModel = trainingLocalDataSource.getTrainingListItems()
            if (listDbModel.isNotEmpty()) {
                result = DataResponse.Success(
                    data = ListState(
                        items = trainingListItemMapper.fromListDbModelToListDomain(listDbModel),
                        isLastPage = true
                    ),
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
            trainingLocalDataSource.getTraining(id)?.let { localTraining ->
                result = DataResponse.Success(
                    data = trainingMapper.fromDbModelToDomain(localTraining),
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

    override suspend fun deleteTraining(id: Long): DataResponse<Long> {
        return trainingRemoteApiDataSource.deleteTraining(id)
    }
}
