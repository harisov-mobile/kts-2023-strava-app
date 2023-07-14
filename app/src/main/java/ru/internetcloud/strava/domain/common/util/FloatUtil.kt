package ru.internetcloud.strava.domain.common.util

fun Float?.orDefault(): Float = this ?: 0f

fun Float.convertToString(): String {
    return if (this == 0f) {
        ""
    } else {
        this.toString()
    }
}
