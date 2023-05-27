package ru.internetcloud.strava.data.profile.repository

import ru.internetcloud.strava.data.profile.cache.datasource.ProfileLocalDataSourceImpl
import ru.internetcloud.strava.data.profile.mapper.ProfileMapper
import ru.internetcloud.strava.data.profile.network.datasource.ProfileRemoteApiDataSourceImpl
import ru.internetcloud.strava.domain.common.model.DataResponse
import ru.internetcloud.strava.domain.profile.ProfileRepository
import ru.internetcloud.strava.domain.profile.model.Profile

class ProfileRepositoryImpl : ProfileRepository {

    private val profileRemoteApiDataSource = ProfileRemoteApiDataSourceImpl()
    private val profileLocalDataSource = ProfileLocalDataSourceImpl()
    private val profileMapper = ProfileMapper()

    override suspend fun getProfile(): DataResponse<Profile> {
        var result = profileRemoteApiDataSource.getProfile()

        if (result is DataResponse.Success) {
            profileLocalDataSource.insertProfile(profileMapper.fromDomainToDbModel(result.data))
        } else {
            profileLocalDataSource.getProfile()?.let { profileDbModel ->
                result = DataResponse.Success(data = profileMapper.fromDbModelToDomain(profileDbModel))
            }
        }

        return result
    }
}
