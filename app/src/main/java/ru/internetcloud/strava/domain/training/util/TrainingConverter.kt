package ru.internetcloud.strava.domain.training.util

import ru.internetcloud.strava.domain.training.model.Training
import ru.internetcloud.strava.domain.training.model.TrainingListItem

class TrainingConverter {

    companion object {
        fun fromTrainingToTrainingListItem(training: Training): TrainingListItem {
            return TrainingListItem(
                id = training.id,
                name = training.name,
                distance = training.distance,
                movingTime = training.movingTime,
                type = training.type,
                startDate = training.startDate
            )
        }
    }
}
