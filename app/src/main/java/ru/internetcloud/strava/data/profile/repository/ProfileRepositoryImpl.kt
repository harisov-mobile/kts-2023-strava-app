package ru.internetcloud.strava.data.profile.repository

import ru.internetcloud.strava.data.profile.cache.datasource.ProfileLocalDataSource
import ru.internetcloud.strava.data.profile.mapper.ProfileMapper
import ru.internetcloud.strava.data.profile.network.datasource.ProfileRemoteApiDataSource
import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.common.model.Source
import ru.internetcloud.strava.domain.profile.ProfileRepository
import ru.internetcloud.strava.domain.profile.model.Profile

class ProfileRepositoryImpl(
    private val profileRemoteApiDataSource: ProfileRemoteApiDataSource,
    private val profileLocalDataSource: ProfileLocalDataSource,
    private val profileMapper: ProfileMapper
) : ProfileRepository {

    override suspend fun getProfile(): DataResponse<Profile> {
        var result = profileRemoteApiDataSource.getProfile()

        if (result is DataResponse.Success) {
            profileLocalDataSource.deleteProfile()
            profileLocalDataSource.insertProfile(profileMapper.fromDomainToDbModel(result.data))
        } else {
            profileLocalDataSource.getProfile()?.let { localProfile ->
                result = DataResponse.Success(
                    data = profileMapper.fromDbModelToDomain(localProfile),
                    source = Source.LocalCache
                )
            }
        }

        return result
    }

    override suspend fun deleteProfileInLocalCache() {
        profileLocalDataSource.deleteProfile()
    }
}
