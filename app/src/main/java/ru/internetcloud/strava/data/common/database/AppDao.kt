package ru.internetcloud.strava.data.common.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.internetcloud.strava.data.profile.cache.model.ProfileDbModel
import ru.internetcloud.strava.data.training.cache.model.TrainingDbModel
import ru.internetcloud.strava.data.training.cache.model.TrainingListItemDbModel

@Dao
interface AppDao {

    @Query("SELECT * FROM profile LIMIT 1")
    suspend fun getProfile(): ProfileDbModel? // будет только одна запись с профилем (или ни одной)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profileDbModel: ProfileDbModel)

    @Query("DELETE FROM training_list_items")
    suspend fun deleteAllTrainingListItems()

    @Query("DELETE FROM trainings")
    suspend fun deleteAllTrainings()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrainingListItems(list: List<TrainingListItemDbModel>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTraining(trainingDbModel: TrainingDbModel)

    @Query("SELECT * FROM training_list_items")
    suspend fun getTrainingListItems(): List<TrainingListItemDbModel>

    @Query("SELECT * FROM trainings WHERE id=:id LIMIT 1")
    suspend fun getTraining(id: Long): TrainingDbModel?
}
