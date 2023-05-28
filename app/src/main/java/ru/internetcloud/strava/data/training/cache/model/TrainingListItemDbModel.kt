package ru.internetcloud.strava.data.training.cache.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "training_list_items")
data class TrainingListItemDbModel(
    @PrimaryKey
    val id: Long,
    val name: String,
    val distance: Float,
    val movingTime: Int,
    val type: String,
    val startDate: Date
)
