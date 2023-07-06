package ru.internetcloud.strava.data.training.network.datasource

import ru.internetcloud.strava.data.common.ErrorMessageConverter
import ru.internetcloud.strava.data.training.mapper.TrainingListItemMapper
import ru.internetcloud.strava.data.training.mapper.TrainingMapper
import ru.internetcloud.strava.data.training.network.api.TrainingApi
import ru.internetcloud.strava.data.training.network.model.TrainingPhotoDTO
import ru.internetcloud.strava.domain.common.list.mvi.model.ListLoadParams
import ru.internetcloud.strava.domain.common.list.mvi.model.ListState
import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.common.model.Source
import ru.internetcloud.strava.domain.common.util.orDefault
import ru.internetcloud.strava.domain.common.util.toInt
import ru.internetcloud.strava.domain.training.model.Training
import ru.internetcloud.strava.domain.training.model.TrainingListItem

class TrainingRemoteApiDataSourceImpl(
    private val trainingApi: TrainingApi,
    private val trainingMapper: TrainingMapper,
    private val trainingListItemMapper: TrainingListItemMapper,
    private val errorMessageConverter: ErrorMessageConverter
) : TrainingRemoteApiDataSource {

    override suspend fun getTrainings(params: ListLoadParams<Unit>): DataResponse<ListState<TrainingListItem>> {
        return try {
            val networkResponse = trainingApi.getTrainings(page = params.page, perPage = params.pageSize)
            if (networkResponse.isSuccessful) {
                val listDTO = networkResponse.body()
                listDTO?.let { currentListDTO ->
                    val list = trainingListItemMapper.fromListDtoToListDomain(currentListDTO)
                    DataResponse.Success(
                        data = ListState(items = list, isLastPage = list.size < params.pageSize),
                        source = Source.RemoteApi
                    )
                } ?: let {
                    DataResponse.Success(
                        data = ListState(items = emptyList(), isLastPage = true),
                        source = Source.RemoteApi
                    )
                }
            } else {
                DataResponse.Error(Exception(errorMessageConverter.getMessageToHTTPCode(networkResponse.code())))
            }
        } catch (e: Exception) {
            DataResponse.Error(Exception(errorMessageConverter.getMessageToException(e)))
        }
    }

    override suspend fun getTraining(id: Long): DataResponse<Training> {
        return try {
            val networkResponse = trainingApi.getTraining(id = id)
            if (networkResponse.isSuccessful) {
                val trainingDTO = networkResponse.body()
                trainingDTO?.let { currentDTO ->
                    val photos = getPhotos(id)
                    DataResponse.Success(
                        data = trainingMapper.fromDtoToDomain(currentDTO, photos),
                        source = Source.RemoteApi
                    )
                } ?: let {
                    DataResponse.Error(exception = IllegalStateException("No training found with id = $id"))
                }
            } else {
                DataResponse.Error(Exception(errorMessageConverter.getMessageToHTTPCode(networkResponse.code())))
            }
        } catch (e: Exception) {
            DataResponse.Error(Exception(errorMessageConverter.getMessageToException(e)))
        }
    }

    private suspend fun getPhotos(id: Long): List<TrainingPhotoDTO> {
        return try {
            val networkResponse = trainingApi.getPhotos(id = id)
            if (networkResponse.isSuccessful) {
                val listDTO = networkResponse.body()
                listDTO ?: let {
                    emptyList()
                }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun addTraining(training: Training): DataResponse<Training> {
        return try {
            val newTrainingDTO = trainingMapper.fromDomainToDto(training)
            val networkResponse = trainingApi.addTraining(
                name = newTrainingDTO.name,
                sport = newTrainingDTO.sport,
                startDate = newTrainingDTO.startDate,
                elapsedTime = newTrainingDTO.elapsedTime,
                description = newTrainingDTO.description.orEmpty(),
                distance = newTrainingDTO.distance,
                trainer = newTrainingDTO.trainer.orDefault().toInt(),
                commute = newTrainingDTO.commute.orDefault().toInt()
            )
            if (networkResponse.isSuccessful) {
                val trainingDTO = networkResponse.body()
                trainingDTO?.let { currentDTO ->
                    DataResponse.Success(
                        data = trainingMapper.fromDtoToDomain(currentDTO, photos = emptyList()),
                        source = Source.RemoteApi
                    )
                } ?: let {
                    DataResponse.Error(exception = IllegalStateException("An error occurred while adding a training."))
                }
            } else {
                DataResponse.Error(Exception(errorMessageConverter.getMessageToHTTPCode(networkResponse.code())))
            }
        } catch (e: Exception) {
            DataResponse.Error(Exception(errorMessageConverter.getMessageToException(e)))
        }
    }

    override suspend fun updateTraining(training: Training): DataResponse<Training> {
        return try {
            val trainingUpdateDTO = trainingMapper.fromDomainToUpdateDto(training)

            val networkResponse = trainingApi.updateTraining(
                id = trainingUpdateDTO.id,
                training = trainingUpdateDTO
            )
            if (networkResponse.isSuccessful) {
                val trainingDTO = networkResponse.body()
                trainingDTO?.let { currentDTO ->
                    val photos = getPhotos(trainingUpdateDTO.id)
                    DataResponse.Success(
                        data = trainingMapper.fromDtoToDomain(currentDTO, photos = photos),
                        source = Source.RemoteApi
                    )
                } ?: let {
                    DataResponse.Error(exception = IllegalStateException("An error occurred while adding a training."))
                }
            } else {
                DataResponse.Error(Exception(errorMessageConverter.getMessageToHTTPCode(networkResponse.code())))
            }
        } catch (e: Exception) {
            DataResponse.Error(Exception(errorMessageConverter.getMessageToException(e)))
        }
    }

    override suspend fun deleteTraining(id: Long): DataResponse<Long> {
        return try {
            val networkResponse = trainingApi.deleteTraining(id = id)
            if (networkResponse.isSuccessful) {
                DataResponse.Success(
                    data = id,
                    source = Source.RemoteApi
                )
            } else {
                DataResponse.Error(Exception(errorMessageConverter.getMessageToHTTPCode(networkResponse.code())))
            }
        } catch (e: Exception) {
            DataResponse.Error(Exception(errorMessageConverter.getMessageToException(e)))
        }
    }
}
