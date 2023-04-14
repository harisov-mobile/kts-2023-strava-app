package ru.internetcloud.strava.domain

import ru.internetcloud.strava.domain.model.DataResponse
import ru.internetcloud.strava.domain.model.Training
import ru.internetcloud.strava.domain.model.TrainingListItem

interface TrainingRepository {

    suspend fun getTrainings(page: Int): DataResponse<List<TrainingListItem>>

    suspend fun getTraining(id: Long): DataResponse<Training>
}
