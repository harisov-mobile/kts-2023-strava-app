package ru.internetcloud.strava.domain.common.model

sealed interface Source {

    object RemoteApi : Source

    object LocalCache : Source
}
