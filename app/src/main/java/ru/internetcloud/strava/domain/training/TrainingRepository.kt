package ru.internetcloud.strava.domain.training

import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.training.model.Training
import ru.internetcloud.strava.domain.training.model.TrainingListItem

interface TrainingRepository {

    suspend fun getTrainings(page: Int): DataResponse<List<TrainingListItem>>

    suspend fun getTraining(id: Long): DataResponse<Training>
}