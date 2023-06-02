package ru.internetcloud.strava.data.training.network.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import ru.internetcloud.strava.data.training.network.model.TrainingDTO
import ru.internetcloud.strava.data.training.network.model.TrainingListItemDTO
import ru.internetcloud.strava.data.training.network.model.TrainingUpdateDTO

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

    @POST("api/v3/activities")
    suspend fun addTraining(
        @Query("name") name: String,
        @Query("type") type: String,
        @Query("sport_type") sportType: String,
        @Query("start_date_local") startDate: String,
        @Query("elapsed_time") elapsedTime: Int,
        @Query("description") description: String,
        @Query("distance") distance: Float,
        @Query("trainer") trainer: Int,
        @Query("commute") commute: Int
    ): Response<TrainingDTO>

    @PUT("api/v3/activities/{id}")
    suspend fun updateTraining(
        @Path(PATH_PARAM_ID)
        id: Long,

        @Body
        training: TrainingUpdateDTO
    ): Response<TrainingDTO>

    companion object {
        private const val QUERY_PARAM_PAGE = "page"
        private const val PATH_PARAM_ID = "id"
    }
}
