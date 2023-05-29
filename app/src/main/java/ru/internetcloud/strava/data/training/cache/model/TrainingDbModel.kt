package ru.internetcloud.strava.data.training.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = TrainingContract.TABLE_NAME)
data class TrainingDbModel(
    @PrimaryKey
    @ColumnInfo(name = TrainingContract.Columns.ID)
    val id: Long,

    @ColumnInfo(name = TrainingContract.Columns.NAME)
    val name: String,

    @ColumnInfo(name = TrainingContract.Columns.DISTANCE)
    val distance: Float,

    @ColumnInfo(name = TrainingContract.Columns.MOVING_TIME)
    val movingTime: Int,

    @ColumnInfo(name = TrainingContract.Columns.TYPE)
    val type: String,

    @ColumnInfo(name = TrainingContract.Columns.START_DATE)
    val startDate: Date,

    @ColumnInfo(name = TrainingContract.Columns.DESCRIPTION)
    val description: String
)
