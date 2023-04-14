package ru.internetcloud.strava.domain.model

data class ProfileWithTrainingList(
    val profile: Profile,
    val trainingList: List<TrainingListItem>
)
