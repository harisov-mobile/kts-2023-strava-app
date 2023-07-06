package ru.internetcloud.strava.data.training.mapper

import ru.internetcloud.strava.data.training.cache.model.LocalTraining
import ru.internetcloud.strava.data.training.network.model.TrainingDTO
import ru.internetcloud.strava.data.training.network.model.TrainingUpdateDTO
import ru.internetcloud.strava.domain.common.util.DateConverter
import ru.internetcloud.strava.domain.common.util.orDefault
import ru.internetcloud.strava.domain.training.model.Training

class TrainingMapper(
    private val dateConverter: DateConverter
) {
    fun fromDtoToDomain(trainingDTO: TrainingDTO): Training {
        return Training(
            id = trainingDTO.id,
            name = trainingDTO.name,
            distance = trainingDTO.distance,
            movingTime = trainingDTO.movingTime,
            elapsedTime = trainingDTO.elapsedTime,
            sport = trainingDTO.sport,
            startDate = dateConverter.fromStringInIso8601ToDate(trainingDTO.startDate),
            description = trainingDTO.description.orEmpty(),
            trainer = trainingDTO.trainer.orDefault(),
            commute = trainingDTO.commute.orDefault()
        )
    }

    fun fromDomainToDbModel(training: Training): LocalTraining {
        return LocalTraining(
            id = training.id,
            name = training.name,
            distance = training.distance,
            movingTime = training.movingTime,
            elapsedTime = training.elapsedTime,
            sport = training.sport,
            startDate = training.startDate,
            description = training.description,
            trainer = training.trainer,
            commute = training.commute
        )
    }

    fun fromDbModelToDomain(localTraining: LocalTraining): Training {
        return Training(
            id = localTraining.id,
            name = localTraining.name,
            distance = localTraining.distance,
            movingTime = localTraining.movingTime,
            elapsedTime = localTraining.elapsedTime,
            sport = localTraining.sport,
            startDate = localTraining.startDate,
            description = localTraining.description,
            trainer = localTraining.trainer,
            commute = localTraining.commute
        )
    }

    fun fromDomainToDto(training: Training): TrainingDTO {
        return TrainingDTO(
            id = training.id,
            name = training.name,
            distance = training.distance,
            movingTime = training.movingTime,
            startDate = dateConverter.getDateISO8601String(training.startDate),
            description = training.description,
            elapsedTime = training.elapsedTime,
            sport = training.sport,
            trainer = training.trainer,
            commute = training.commute
        )
    }

    fun fromDomainToUpdateDto(training: Training): TrainingUpdateDTO {
        return TrainingUpdateDTO(
            id = training.id,
            name = training.name,
            description = training.description,
            sport = training.sport,
            trainer = training.trainer,
            commute = training.commute,
            startDate = dateConverter.getDateISO8601String(training.startDate),
            elapsedTime = training.elapsedTime,
            distance = training.distance
        )
    }
}
