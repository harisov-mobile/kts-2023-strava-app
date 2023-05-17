package ru.internetcloud.strava.data.training.mapper

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
}
