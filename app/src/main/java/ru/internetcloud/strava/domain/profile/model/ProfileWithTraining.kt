package ru.internetcloud.strava.domain.profile.model

import ru.internetcloud.strava.domain.training.model.Training

data class ProfileWithTraining(
    val profile: Profile,
    val training: Training
)
