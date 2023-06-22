package ru.internetcloud.strava.data.profile.mapper

import ru.internetcloud.strava.data.profile.cache.model.LocalProfile
import ru.internetcloud.strava.data.profile.network.model.ProfileDTO
import ru.internetcloud.strava.domain.common.util.orDefault
import ru.internetcloud.strava.domain.profile.model.Profile

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

    fun fromDbModelToDomain(localProfile: LocalProfile): Profile {
        return Profile(
            id = localProfile.id,
            firstName = localProfile.firstName,
            lastName = localProfile.lastName,
            city = localProfile.city,
            state = localProfile.state,
            country = localProfile.country,
            sex = localProfile.sex,
            imageUrlMedium = localProfile.imageUrlMedium,
            imageUrl = localProfile.imageUrl,
            friendCount = localProfile.friendCount,
            followerCount = localProfile.followerCount
        )
    }

    fun fromDomainToDbModel(profile: Profile): LocalProfile {
        return LocalProfile(
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
