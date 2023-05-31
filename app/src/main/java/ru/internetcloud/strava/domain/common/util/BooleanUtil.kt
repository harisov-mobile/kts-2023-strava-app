package ru.internetcloud.strava.domain.common.util

fun Boolean.toInt(): Int = if (this) 1 else 0

fun Boolean?.orDefault(): Boolean = this ?: false
