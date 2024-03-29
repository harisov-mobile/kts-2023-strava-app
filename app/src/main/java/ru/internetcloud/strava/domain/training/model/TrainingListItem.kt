package ru.internetcloud.strava.domain.training.model

import java.util.Date

data class TrainingListItem(
    val id: Long,
    val name: String,
    val distance: Float,
    val movingTime: Int,
    val sport: String,
    val startDate: Date
)
