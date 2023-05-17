package ru.internetcloud.strava.data.repository

import ru.internetcloud.strava.data.mapper.TrainingListItemMapper
import ru.internetcloud.strava.data.mapper.TrainingMapper
import ru.internetcloud.strava.data.network.api.StravaApiFactory
import ru.internetcloud.strava.domain.TrainingRepository
import ru.internetcloud.strava.domain.model.DataResponse
import ru.internetcloud.strava.domain.model.Training
import ru.internetcloud.strava.domain.model.TrainingListItem

class TrainingRepositoryImpl : TrainingRepository {

    private val trainingMapper = TrainingMapper()
    private val trainingListItemMapper = TrainingListItemMapper()

    override suspend fun getTrainings(page: Int): DataResponse<List<TrainingListItem>> {
        var result: DataResponse<List<TrainingListItem>>

        try {
            val networkResponse = StravaApiFactory.trainingApi.getTrainings(page = page)
            if (networkResponse.isSuccessful) {
                val listDTO = networkResponse.body()
                result = listDTO?.let { currentListDTO ->
                    val list = trainingListItemMapper.fromListDtoToListDomain(currentListDTO)
                    DataResponse.Success(list)
                } ?: let {
                    DataResponse.Success(emptyList<TrainingListItem>())
                }
            } else {
                result = DataResponse.Error(Exception(networkResponse.errorBody()?.string() ?: ""))
            }
        } catch (e: Exception) {
            result = DataResponse.Error(e)
        }
        return result
    }

    override suspend fun getTraining(id: Long): DataResponse<Training> {
        var result: DataResponse<Training>

        try {
            val networkResponse = StravaApiFactory.trainingApi.getTraining(id = id)
            if (networkResponse.isSuccessful) {
                val trainingDTO = networkResponse.body()
                result = trainingDTO?.let { currentDTO ->
                    DataResponse.Success(trainingMapper.fromDtoToDomain(currentDTO))
                } ?: let {
                    DataResponse.Error(exception = java.lang.IllegalStateException("No activity found with id = $id"))
                }
            } else {
                result = DataResponse.Error(Exception(networkResponse.errorBody()?.string() ?: ""))
            }
        } catch (e: Exception) {
            result = DataResponse.Error(e)
        }
        return result
    }
}
