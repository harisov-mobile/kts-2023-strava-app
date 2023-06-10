package ru.internetcloud.strava.data.profile.cache.datasource

import ru.internetcloud.strava.data.common.database.AppDatabaseHolder
import ru.internetcloud.strava.data.profile.cache.model.LocalProfile

class ProfileLocalDataSourceImpl : ProfileLocalDataSource {

    private val appDao = AppDatabaseHolder.appDao // потом будет dependecy injecton вместо этой переменной

    override suspend fun getProfile(): LocalProfile? {
        return appDao.getProfile()
    }

    override suspend fun insertProfile(localProfile: LocalProfile) {
        appDao.insertProfile(localProfile)
    }

    override suspend fun deleteProfile() {
        appDao.deleteProfile()
    }
}
