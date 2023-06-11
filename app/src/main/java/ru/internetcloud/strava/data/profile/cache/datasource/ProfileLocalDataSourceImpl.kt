package ru.internetcloud.strava.data.profile.cache.datasource

import ru.internetcloud.strava.data.common.database.AppDao
import ru.internetcloud.strava.data.profile.cache.model.LocalProfile

class ProfileLocalDataSourceImpl(
    private val appDao: AppDao
) : ProfileLocalDataSource {

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
