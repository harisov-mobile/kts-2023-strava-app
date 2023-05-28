package ru.internetcloud.strava.data.training.mapper

import ru.internetcloud.strava.data.training.cache.model.TrainingDbModel
import ru.internetcloud.strava.data.training.network.model.TrainingDTO
import ru.internetcloud.strava.domain.training.model.Training
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
            description = trainingDTO.description.orEmpty()
        )
    }

    fun fromDomainToDbModel(training: Training): TrainingDbModel {
        return TrainingDbModel(
            id = training.id,
            name = training.name,
            distance = training.distance,
            movingTime = training.movingTime,
            type = training.type,
            startDate = training.startDate,
            description = training.description
        )
    }

    fun fromDbModelToDomain(trainingDbModel: TrainingDbModel): Training {
        return Training(
            id = trainingDbModel.id,
            name = trainingDbModel.name,
            distance = trainingDbModel.distance,
            movingTime = trainingDbModel.movingTime,
            type = trainingDbModel.type,
            startDate = trainingDbModel.startDate,
            description = trainingDbModel.description
        )
    }
}
