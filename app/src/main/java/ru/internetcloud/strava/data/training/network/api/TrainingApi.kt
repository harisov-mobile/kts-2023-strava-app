package ru.internetcloud.strava.data.training.network.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.internetcloud.strava.data.training.network.model.TrainingDTO
import ru.internetcloud.strava.data.training.network.model.TrainingListItemDTO

interface TrainingApi {

    @GET("api/v3/activities")
    suspend fun getTrainings(
        @Query(QUERY_PARAM_PAGE)
        page: Int
    ): Response<List<TrainingListItemDTO>>

    @GET("api/v3/activities/{id}")
    suspend fun getTraining(
        @Path(PATH_PARAM_ID)
        id: Long
    ): Response<TrainingDTO>

    companion object {
        private const val QUERY_PARAM_PAGE = "page"
        private const val PATH_PARAM_ID = "id"
    }
}
