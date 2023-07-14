package ru.internetcloud.strava.data.training.mapper

import ru.internetcloud.strava.data.training.cache.model.LocalTrainingListItem
import ru.internetcloud.strava.data.training.network.model.TrainingListItemDTO
import ru.internetcloud.strava.domain.common.util.DateConverter
import ru.internetcloud.strava.domain.training.model.TrainingListItem

class TrainingListItemMapper(
    private val dateConverter: DateConverter
) {

    private fun fromDtoToDomain(trainingDTO: TrainingListItemDTO): TrainingListItem {
        return TrainingListItem(
            id = trainingDTO.id,
            name = trainingDTO.name,
            distance = trainingDTO.distance,
            movingTime = trainingDTO.movingTime,
            sport = trainingDTO.sport,
            startDate = dateConverter.fromStringInIso8601ToDate(trainingDTO.startDate)
        )
    }

    fun fromListDtoToListDomain(listDTO: List<TrainingListItemDTO>): List<TrainingListItem> {
        return listDTO.map { fromDtoToDomain(it) }
    }

    private fun fromDomainToDbModel(trainingListItem: TrainingListItem): LocalTrainingListItem {
        return LocalTrainingListItem(
            id = trainingListItem.id,
            name = trainingListItem.name,
            distance = trainingListItem.distance,
            movingTime = trainingListItem.movingTime,
            sport = trainingListItem.sport,
            startDate = trainingListItem.startDate
        )
    }

    private fun fromDbModelToDomain(localTrainingListItem: LocalTrainingListItem): TrainingListItem {
        return TrainingListItem(
            id = localTrainingListItem.id,
            name = localTrainingListItem.name,
            distance = localTrainingListItem.distance,
            movingTime = localTrainingListItem.movingTime,
            sport = localTrainingListItem.sport,
            startDate = localTrainingListItem.startDate
        )
    }

    fun fromListDomainToListDbModel(list: List<TrainingListItem>): List<LocalTrainingListItem> {
        return list.map { fromDomainToDbModel(it) }
    }

    fun fromListDbModelToListDomain(list: List<LocalTrainingListItem>): List<TrainingListItem> {
        return list.map { fromDbModelToDomain(it) }
    }
}
