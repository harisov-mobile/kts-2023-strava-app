package ru.internetcloud.strava.domain.util

import ru.internetcloud.strava.domain.model.Training
import ru.internetcloud.strava.domain.model.TrainingListItem

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
