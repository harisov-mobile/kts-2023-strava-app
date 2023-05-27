package ru.internetcloud.strava.data.profile.cache.datasource

import ru.internetcloud.strava.data.common.database.AppDatabaseHolder
import ru.internetcloud.strava.data.profile.mapper.ProfileMapper
import ru.internetcloud.strava.domain.profile.model.Profile

class ProfileLocalDataSourceImpl : ProfileLocalDataSource {

    private val appDao = AppDatabaseHolder.appDao // потом будет dependecy injecton вместо этой переменной
    private val profileMapper = ProfileMapper()

    override suspend fun getProfile(): Profile {
        return profileMapper.fromDbModelToDomain(appDao.getProfile())
    }
}
