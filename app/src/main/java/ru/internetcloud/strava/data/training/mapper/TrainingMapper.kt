package ru.internetcloud.strava.data.training.mapper

import ru.internetcloud.strava.data.training.cache.model.TrainingDbModel
import ru.internetcloud.strava.data.training.network.model.TrainingDTO
import ru.internetcloud.strava.data.training.network.model.TrainingUpdateDTO
import ru.internetcloud.strava.domain.common.util.orDefault
import ru.internetcloud.strava.domain.training.model.Training
import ru.internetcloud.strava.presentation.util.DateTimeConverter

class TrainingMapper {

    fun fromDtoToDomain(trainingDTO: TrainingDTO): Training {
        return Training(
            id = trainingDTO.id,
            name = trainingDTO.name,
            distance = trainingDTO.distance,
            movingTime = trainingDTO.movingTime,
            elapsedTime = trainingDTO.elapsedTime,
            type = trainingDTO.type,
            sportType = trainingDTO.sportType,
            startDate = DateTimeConverter.fromStringToDate(trainingDTO.startDate),
            description = trainingDTO.description.orEmpty(),
            trainer = trainingDTO.trainer.orDefault(),
            commute = trainingDTO.commute.orDefault()
        )
    }

    fun fromDomainToDbModel(training: Training): TrainingDbModel {
        return TrainingDbModel(
            id = training.id,
            name = training.name,
            distance = training.distance,
            movingTime = training.movingTime,
            elapsedTime = training.elapsedTime,
            type = training.type,
            sportType = training.sportType,
            startDate = training.startDate,
            description = training.description,
            trainer = training.trainer,
            commute = training.commute
        )
    }

    fun fromDbModelToDomain(trainingDbModel: TrainingDbModel): Training {
        return Training(
            id = trainingDbModel.id,
            name = trainingDbModel.name,
            distance = trainingDbModel.distance,
            movingTime = trainingDbModel.movingTime,
            elapsedTime = trainingDbModel.elapsedTime,
            type = trainingDbModel.type,
            sportType = trainingDbModel.sportType,
            startDate = trainingDbModel.startDate,
            description = trainingDbModel.description,
            trainer = trainingDbModel.trainer,
            commute = trainingDbModel.commute
        )
    }

    fun fromDomainToDto(training: Training): TrainingDTO {
        return TrainingDTO(
            id = training.id,
            name = training.name,
            distance = training.distance,
            movingTime = training.movingTime,
            type = training.type,
            startDate = training.startDate.toString(),
            description = training.description,
            elapsedTime = 0,
            sportType = "",
            trainer = false,
            commute = false
        )
    }

    fun fromDomainToUpdateDto(training: Training): TrainingUpdateDTO {
        return TrainingUpdateDTO(
            id = training.id,
            name = training.name,
            description = training.description,
            sportType = "",
            trainer = false,
            commute = false
        )
    }
}
