package ru.internetcloud.strava.data.common.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.internetcloud.strava.data.profile.cache.model.ProfileContract
import ru.internetcloud.strava.data.profile.cache.model.ProfileDbModel
import ru.internetcloud.strava.data.training.cache.model.TrainingContract
import ru.internetcloud.strava.data.training.cache.model.TrainingDbModel
import ru.internetcloud.strava.data.training.cache.model.TrainingListItemContract
import ru.internetcloud.strava.data.training.cache.model.TrainingListItemDbModel

@Dao
interface AppDao {

    @Query("SELECT * FROM ${ProfileContract.TABLE_NAME} LIMIT 1")
    suspend fun getProfile(): ProfileDbModel? // будет только одна запись с профилем (или ни одной)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profileDbModel: ProfileDbModel)

    @Query("DELETE FROM ${ProfileContract.TABLE_NAME}")
    suspend fun deleteProfile()

    @Query("DELETE FROM ${TrainingListItemContract.TABLE_NAME}")
    suspend fun deleteAllTrainingListItems()

    @Query("DELETE FROM ${TrainingContract.TABLE_NAME}")
    suspend fun deleteAllTrainings()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrainingListItems(list: List<TrainingListItemDbModel>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTraining(trainingDbModel: TrainingDbModel)

    @Query("SELECT * FROM ${TrainingListItemContract.TABLE_NAME}" +
            " ORDER BY ${TrainingListItemContract.Columns.START_DATE} DESC")
    suspend fun getTrainingListItems(): List<TrainingListItemDbModel>

    @Query("SELECT * FROM ${TrainingContract.TABLE_NAME} WHERE ${TrainingContract.Columns.ID} = :id LIMIT 1")
    suspend fun getTraining(id: Long): TrainingDbModel?
}
