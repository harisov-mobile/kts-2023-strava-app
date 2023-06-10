package ru.internetcloud.strava.data.profile.repository

import ru.internetcloud.strava.data.profile.cache.datasource.ProfileLocalDataSourceImpl
import ru.internetcloud.strava.data.profile.mapper.ProfileMapper
import ru.internetcloud.strava.data.profile.network.datasource.ProfileRemoteApiDataSourceImpl
import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.common.model.Source
import ru.internetcloud.strava.domain.profile.ProfileRepository
import ru.internetcloud.strava.domain.profile.model.Profile

class ProfileRepositoryImpl : ProfileRepository {

    private val profileRemoteApiDataSource = ProfileRemoteApiDataSourceImpl()
    private val profileLocalDataSource = ProfileLocalDataSourceImpl()
    private val profileMapper = ProfileMapper()

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
