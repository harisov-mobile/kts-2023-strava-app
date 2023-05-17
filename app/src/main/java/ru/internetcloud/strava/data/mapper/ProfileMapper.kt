package ru.internetcloud.strava.data.mapper

import ru.internetcloud.strava.data.network.model.ProfileDTO
import ru.internetcloud.strava.domain.model.Profile

class ProfileMapper {

    fun fromDtoToDomain(profileDTO: ProfileDTO): Profile {
        return Profile(
            id = profileDTO.id,
            firstName = profileDTO.firstname ?: "",
            lastName = profileDTO.lastname ?: "",
            city = profileDTO.city ?: "",
            state = profileDTO.state ?: "",
            country = profileDTO.country ?: "",
            sex = profileDTO.sex ?: "",
            imageUrlMedium = profileDTO.profileMedium ?: "",
            imageUrl = profileDTO.profile ?: "",
            friendCount = profileDTO.friendCount ?: 0,
            followerCount = profileDTO.followerCount ?: 0
        )
    }
}
