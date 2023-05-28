package ru.internetcloud.strava.data.common.database

import androidx.room.TypeConverter
import java.util.Date

class DatabaseTypeConverter {

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(millisec: Long?): Date? {
        return millisec?.let {
            Date(it)
        }
    }
}
