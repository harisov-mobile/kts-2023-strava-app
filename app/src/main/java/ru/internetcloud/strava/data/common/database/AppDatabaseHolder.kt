package ru.internetcloud.strava.data.common.database

import android.app.Application

object AppDatabaseHolder {

    private var _appDao: AppDao? = null
    val appDao: AppDao
        get() = _appDao ?: throw IllegalStateException("AppDao is not initialized")

    fun init(application: Application) {
        _appDao = AppDatabase.getInstance(application).appDao()
    }
}
