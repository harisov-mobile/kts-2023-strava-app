package ru.internetcloud.strava.data.training.repository

import ru.internetcloud.strava.data.common.StravaApiFactory
import ru.internetcloud.strava.data.training.mapper.TrainingListItemMapper
import ru.internetcloud.strava.data.training.mapper.TrainingMapper
import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.training.TrainingRepository
import ru.internetcloud.strava.domain.training.model.Training
import ru.internetcloud.strava.domain.training.model.TrainingListItem

class TrainingRepositoryImpl : TrainingRepository {

    private val trainingMapper = TrainingMapper()
    private val trainingListItemMapper = TrainingListItemMapper()

    override suspend fun getTrainings(page: Int): DataResponse<List<TrainingListItem>> {
        var result: DataResponse<List<TrainingListItem>>

        try {
            val networkResponse = StravaApiFactory.trainingApi.getTrainings(page = page)
            result = if (networkResponse.isSuccessful) {
                val listDTO = networkResponse.body()
                listDTO?.let { currentListDTO ->
                    val list = trainingListItemMapper.fromListDtoToListDomain(currentListDTO)
                    DataResponse.Success(list)
                } ?: let {
                    DataResponse.Success(emptyList())
                }
            } else {
                DataResponse.Error(Exception(networkResponse.errorBody()?.string().orEmpty()))
            }
        } catch (e: Exception) {
            result = DataResponse.Error(e)
        }
        return result
    }

    override suspend fun getTraining(id: Long): DataResponse<Training> {
        val result: DataResponse<Training> = try {
            val networkResponse = StravaApiFactory.trainingApi.getTraining(id = id)
            if (networkResponse.isSuccessful) {
                val trainingDTO = networkResponse.body()
                trainingDTO?.let { currentDTO ->
                    DataResponse.Success(trainingMapper.fromDtoToDomain(currentDTO))
                } ?: let {
                    DataResponse.Error(exception = IllegalStateException("No training found with id = $id"))
                }
            } else {
                DataResponse.Error(Exception(networkResponse.errorBody()?.string().orEmpty()))
            }
        } catch (e: Exception) {
            DataResponse.Error(e)
        }
        return result
    }
}
