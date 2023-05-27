package ru.internetcloud.strava.data.common.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.internetcloud.strava.data.profile.cache.model.ProfileDbModel

@Dao
interface AppDao {

    @Query("SELECT * FROM profile LIMIT 1")
    suspend fun getProfile(): ProfileDbModel? // будет только одна запись с профилем (или ни одной)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profileDbModel: ProfileDbModel)
}
