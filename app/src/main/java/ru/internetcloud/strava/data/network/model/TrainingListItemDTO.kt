package ru.internetcloud.strava.data.network.model

data class TrainingListItemDTO(
    val id: Long,
    val name: String,
    val distance: Float,
    val moving_time: Int,
    val athleteid: Long,
    val type: String,
    val start_date: String
)
