package ru.internetcloud.strava.domain.model

import java.util.Date

data class TrainingListItem(
    val id: Long,
    val name: String,
    val distance: Float,
    val movingTime: Int,
    val athleteId: Long,
    val type: String,
    val startDate: Date
)
