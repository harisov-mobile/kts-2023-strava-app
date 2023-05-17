package ru.internetcloud.strava.domain.profile.model

import ru.internetcloud.strava.domain.training.model.TrainingListItem

data class ProfileWithTrainingList(
    val profile: Profile,
    val trainingList: List<TrainingListItem>
)
