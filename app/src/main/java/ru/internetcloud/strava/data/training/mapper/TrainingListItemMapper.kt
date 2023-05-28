package ru.internetcloud.strava.data.training.mapper

import ru.internetcloud.strava.data.training.cache.model.TrainingListItemDbModel
import ru.internetcloud.strava.data.training.network.model.TrainingListItemDTO
import ru.internetcloud.strava.domain.training.model.TrainingListItem
import ru.internetcloud.strava.presentation.util.DateTimeConverter

class TrainingListItemMapper {

    private fun fromDtoToDomain(trainingDTO: TrainingListItemDTO): TrainingListItem {
        return TrainingListItem(
            id = trainingDTO.id,
            name = trainingDTO.name,
            distance = trainingDTO.distance,
            movingTime = trainingDTO.movingTime,
            type = trainingDTO.type,
            startDate = DateTimeConverter.fromStringToDate(trainingDTO.startDate)
        )
    }

    fun fromListDtoToListDomain(listDTO: List<TrainingListItemDTO>): List<TrainingListItem> {
        return listDTO.map { fromDtoToDomain(it) }
    }

    private fun fromDomainToDbModel(trainingListItem: TrainingListItem): TrainingListItemDbModel {
        return TrainingListItemDbModel(
            id = trainingListItem.id,
            name = trainingListItem.name,
            distance = trainingListItem.distance,
            movingTime = trainingListItem.movingTime,
            type = trainingListItem.type,
            startDate = trainingListItem.startDate
        )
    }

    private fun fromDbModelToDomain(trainingListItemDbModel: TrainingListItemDbModel): TrainingListItem {
        return TrainingListItem(
            id = trainingListItemDbModel.id,
            name = trainingListItemDbModel.name,
            distance = trainingListItemDbModel.distance,
            movingTime = trainingListItemDbModel.movingTime,
            type = trainingListItemDbModel.type,
            startDate = trainingListItemDbModel.startDate
        )
    }

    fun fromListDomainToListDbModel(list: List<TrainingListItem>): List<TrainingListItemDbModel> {
        return list.map { fromDomainToDbModel(it) }
    }

    fun fromListDbModelToListDomain(list: List<TrainingListItemDbModel>): List<TrainingListItem> {
        return list.map { fromDbModelToDomain(it) }
    }
}
