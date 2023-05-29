package ru.internetcloud.strava.data.training.network.datasource

import ru.internetcloud.strava.data.common.ErrorMessageConverter
import ru.internetcloud.strava.data.common.StravaApiFactory
import ru.internetcloud.strava.data.training.mapper.TrainingListItemMapper
import ru.internetcloud.strava.data.training.mapper.TrainingMapper
import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.common.model.Source
import ru.internetcloud.strava.domain.common.util.toInt
import ru.internetcloud.strava.domain.training.model.Training
import ru.internetcloud.strava.domain.training.model.TrainingListItem

class TrainingRemoteApiDataSourceImpl : TrainingRemoteApiDataSource {

    private val trainingMapper = TrainingMapper()
    private val trainingListItemMapper = TrainingListItemMapper()

    override suspend fun getTrainings(page: Int): DataResponse<List<TrainingListItem>> {
        return try {
            val networkResponse = StravaApiFactory.trainingApi.getTrainings(page = page)
            if (networkResponse.isSuccessful) {
                val listDTO = networkResponse.body()
                listDTO?.let { currentListDTO ->
                    val list = trainingListItemMapper.fromListDtoToListDomain(currentListDTO)
                    DataResponse.Success(list, source = Source.RemoteApi)
                } ?: let {
                    DataResponse.Success(emptyList(), source = Source.RemoteApi)
                }
            } else {
                DataResponse.Error(Exception(ErrorMessageConverter.getMessageToHTTPCode(networkResponse.code())))
            }
        } catch (e: Exception) {
            DataResponse.Error(Exception(ErrorMessageConverter.getMessageToException(e)))
        }
    }

    override suspend fun getTraining(id: Long): DataResponse<Training> {
        return try {
            val networkResponse = StravaApiFactory.trainingApi.getTraining(id = id)
            if (networkResponse.isSuccessful) {
                val trainingDTO = networkResponse.body()
                trainingDTO?.let { currentDTO ->
                    DataResponse.Success(
                        data = trainingMapper.fromDtoToDomain(currentDTO),
                        source = Source.RemoteApi
                    )
                } ?: let {
                    DataResponse.Error(exception = IllegalStateException("No training found with id = $id"))
                }
            } else {
                DataResponse.Error(Exception(ErrorMessageConverter.getMessageToHTTPCode(networkResponse.code())))
            }
        } catch (e: Exception) {
            DataResponse.Error(Exception(ErrorMessageConverter.getMessageToException(e)))
        }
    }

    override suspend fun addTraining(training: Training): DataResponse<Training> {
        return try {

            val newTrainingDTO = trainingMapper.fromDomainToDto(training)

            val networkResponse = StravaApiFactory.trainingApi.addTraining(
                name = newTrainingDTO.name,
                type = newTrainingDTO.type,
                sportType = newTrainingDTO.sportType,
                startDate = newTrainingDTO.startDate,
                elapsedTime = newTrainingDTO.elapsedTime,
                description = newTrainingDTO.description.orEmpty(),
                distance = newTrainingDTO.distance,
                trainer = newTrainingDTO.trainer.toInt(),
                commute = newTrainingDTO.commute.toInt()
            )
            if (networkResponse.isSuccessful) {
                val trainingDTO = networkResponse.body()
                trainingDTO?.let { currentDTO ->
                    DataResponse.Success(
                        data = trainingMapper.fromDtoToDomain(currentDTO),
                        source = Source.RemoteApi
                    )
                } ?: let {
                    DataResponse.Error(exception = IllegalStateException("An error occurred while adding a training."))
                }
            } else {
                DataResponse.Error(Exception(ErrorMessageConverter.getMessageToHTTPCode(networkResponse.code())))
            }
        } catch (e: Exception) {
            DataResponse.Error(Exception(ErrorMessageConverter.getMessageToException(e)))
        }
    }
}
