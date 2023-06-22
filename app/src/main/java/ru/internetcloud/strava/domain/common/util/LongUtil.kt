package ru.internetcloud.strava.domain.common.util

fun Long?.orDefault(): Long = this ?: 0
