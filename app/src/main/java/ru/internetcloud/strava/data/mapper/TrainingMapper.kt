package ru.internetcloud.strava.data.mapper

import ru.internetcloud.strava.data.network.model.TrainingDTO
import ru.internetcloud.strava.domain.model.Training
import ru.internetcloud.strava.presentation.util.DateTimeConverter

class TrainingMapper {

    fun fromDtoToDomain(trainingDTO: TrainingDTO): Training {
        return Training(
            id = trainingDTO.id,
            name = trainingDTO.name,
            distance = trainingDTO.distance,
            movingTime = trainingDTO.movingTime,
            type = trainingDTO.type,
            startDate = DateTimeConverter.fromStringToDate(trainingDTO.startDate),
            description = trainingDTO.description ?: ""
        )
    }
}
