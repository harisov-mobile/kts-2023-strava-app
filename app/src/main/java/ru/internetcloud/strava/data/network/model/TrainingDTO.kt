package ru.internetcloud.strava.data.network.model

data class TrainingDTO(
    val id: Long,
    val name: String,
    val distance: Float,
    val moving_time: Int,
    val athleteid: Long,
    val type: String,
    val start_date: String,
    val description: String?
)
