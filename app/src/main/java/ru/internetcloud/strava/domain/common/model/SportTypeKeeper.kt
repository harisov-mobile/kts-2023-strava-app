package ru.internetcloud.strava.domain.common.model

interface SportTypeKeeper {
    fun getSportTypes(): List<String>
}
