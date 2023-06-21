package ru.internetcloud.strava.domain.training

import ru.internetcloud.strava.domain.common.list.mvi.model.ListLoadParams
import ru.internetcloud.strava.domain.common.list.mvi.model.ListState
import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.training.model.Training
import ru.internetcloud.strava.domain.training.model.TrainingListItem

interface TrainingRepository {

    suspend fun getTrainings(params: ListLoadParams<Unit>): DataResponse<ListState<TrainingListItem>>

    suspend fun getTrainingsWithLocalCache(params: ListLoadParams<Unit>): DataResponse<ListState<TrainingListItem>>

    suspend fun getTraining(id: Long): DataResponse<Training>

    suspend fun deleteTrainingsInLocalCache()

    suspend fun addTraining(training: Training): DataResponse<Training>

    suspend fun updateTraining(training: Training): DataResponse<Training>

    suspend fun deleteTraining(id: Long): DataResponse<Long>
}
