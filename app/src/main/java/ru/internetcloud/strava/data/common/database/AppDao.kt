package ru.internetcloud.strava.data.common.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.internetcloud.strava.data.profile.cache.model.LocalProfile
import ru.internetcloud.strava.data.profile.cache.model.ProfileContract
import ru.internetcloud.strava.data.training.cache.model.LocalTraining
import ru.internetcloud.strava.data.training.cache.model.LocalTrainingListItem
import ru.internetcloud.strava.data.training.cache.model.TrainingContract
import ru.internetcloud.strava.data.training.cache.model.TrainingListItemContract

@Dao
interface AppDao {

    @Query("SELECT * FROM ${ProfileContract.TABLE_NAME} LIMIT 1")
    suspend fun getProfile(): LocalProfile? // будет только одна запись с профилем (или ни одной)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(localProfile: LocalProfile)

    @Query("DELETE FROM ${ProfileContract.TABLE_NAME}")
    suspend fun deleteProfile()

    @Query("DELETE FROM ${TrainingListItemContract.TABLE_NAME}")
    suspend fun deleteAllTrainingListItems()

    @Query("DELETE FROM ${TrainingContract.TABLE_NAME}")
    suspend fun deleteAllTrainings()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrainingListItems(list: List<LocalTrainingListItem>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTraining(localTraining: LocalTraining)

    @Query("SELECT * FROM ${TrainingListItemContract.TABLE_NAME}" +
            " ORDER BY ${TrainingListItemContract.Columns.START_DATE} DESC")
    suspend fun getTrainingListItems(): List<LocalTrainingListItem>

    @Query("SELECT * FROM ${TrainingContract.TABLE_NAME} WHERE ${TrainingContract.Columns.ID} = :id LIMIT 1")
    suspend fun getTraining(id: Long): LocalTraining?
}
