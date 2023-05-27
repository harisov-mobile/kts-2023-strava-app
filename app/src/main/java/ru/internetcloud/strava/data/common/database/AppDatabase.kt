package ru.internetcloud.strava.data.common.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.internetcloud.strava.data.profile.cache.model.ProfileDbModel

@Database(entities = [ProfileDbModel::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao

    // пока сделаю без dependency injection
    companion object {

        @Volatile
        private var instance: AppDatabase? = null
        private var lock = Any()

        private const val DB_NAME = "strava.db"

        fun getInstance(application: Application): AppDatabase {
            instance?.let {
                return it
            }
            synchronized(lock) {
                instance?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    application,
                    AppDatabase::class.java,
                    DB_NAME
                ).build()

                instance = db
                return db
            }
        }
    }
}
