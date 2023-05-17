package ru.internetcloud.strava.domain.training.model

import java.util.Date

data class Training(
    val id: Long,
    val name: String,
    val distance: Float,
    val movingTime: Int,
    val type: String,
    val startDate: Date,
    val description: String
)
