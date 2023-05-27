package ru.internetcloud.strava.data.profile.mapper

import ru.internetcloud.strava.data.profile.cache.model.ProfileDbModel
import ru.internetcloud.strava.data.profile.network.model.ProfileDTO
import ru.internetcloud.strava.domain.profile.model.Profile
import ru.internetcloud.strava.presentation.util.orDefault

class ProfileMapper {

    fun fromDtoToDomain(profileDTO: ProfileDTO): Profile {
        return Profile(
            id = profileDTO.id,
            firstName = profileDTO.firstname.orEmpty(),
            lastName = profileDTO.lastname.orEmpty(),
            city = profileDTO.city.orEmpty(),
            state = profileDTO.state.orEmpty(),
            country = profileDTO.country.orEmpty(),
            sex = profileDTO.sex.orEmpty(),
            imageUrlMedium = profileDTO.profileMedium.orEmpty(),
            imageUrl = profileDTO.profile.orEmpty(),
            friendCount = profileDTO.friendCount.orDefault(),
            followerCount = profileDTO.followerCount.orDefault()
        )
    }

    fun fromDbModelToDomain(profileDbModel: ProfileDbModel): Profile {
        return Profile(
            id = profileDbModel.id,
            firstName = profileDbModel.firstName,
            lastName = profileDbModel.lastName,
            city = profileDbModel.city,
            state = profileDbModel.state,
            country = profileDbModel.country,
            sex = profileDbModel.sex,
            imageUrlMedium = profileDbModel.imageUrlMedium,
            imageUrl = profileDbModel.imageUrl,
            friendCount = profileDbModel.friendCount,
            followerCount = profileDbModel.followerCount
        )
    }

    fun fromDomainToDbModel(profile: Profile): ProfileDbModel {
        return ProfileDbModel(
            id = profile.id,
            firstName = profile.firstName,
            lastName = profile.lastName,
            city = profile.city,
            state = profile.state,
            country = profile.country,
            sex = profile.sex,
            imageUrlMedium = profile.imageUrlMedium,
            imageUrl = profile.imageUrl,
            friendCount = profile.friendCount,
            followerCount = profile.followerCount
        )
    }
}
