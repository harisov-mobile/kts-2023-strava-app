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
            imageUrlMedium = profileDTO.profile_medium ?: "",
            imageUrl = profileDTO.profile ?: "",
            friendCount = profileDTO.friend_count ?: 0,
            followerCount = profileDTO.follower_count ?: 0
        )
    }
}
