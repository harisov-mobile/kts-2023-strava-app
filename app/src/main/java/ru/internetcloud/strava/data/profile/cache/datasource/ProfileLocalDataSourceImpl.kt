package ru.internetcloud.strava.data.profile.cache.datasource

import ru.internetcloud.strava.data.common.database.AppDatabaseHolder
import ru.internetcloud.strava.data.profile.cache.model.ProfileDbModel

class ProfileLocalDataSourceImpl : ProfileLocalDataSource {

    private val appDao = AppDatabaseHolder.appDao // потом будет dependecy injecton вместо этой переменной

    override suspend fun getProfile(): ProfileDbModel? {
        return appDao.getProfile()
    }

    override suspend fun insertProfile(profileDbModel: ProfileDbModel) {
        appDao.insertProfile(profileDbModel)
    }

    override suspend fun deleteProfile() {
        appDao.deleteProfile()
    }
}
