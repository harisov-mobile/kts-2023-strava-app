package ru.internetcloud.strava.data.training.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = TrainingListItemContract.TABLE_NAME)
data class TrainingListItemDbModel(
    @PrimaryKey
    @ColumnInfo(name = TrainingListItemContract.Columns.ID)
    val id: Long,

    @ColumnInfo(name = TrainingListItemContract.Columns.NAME)
    val name: String,

    @ColumnInfo(name = TrainingListItemContract.Columns.DISTANCE)
    val distance: Float,

    @ColumnInfo(name = TrainingListItemContract.Columns.MOVING_TIME)
    val movingTime: Int,

    @ColumnInfo(name = TrainingListItemContract.Columns.TYPE)
    val type: String,

    @ColumnInfo(name = TrainingListItemContract.Columns.START_DATE)
    val startDate: Date
)
