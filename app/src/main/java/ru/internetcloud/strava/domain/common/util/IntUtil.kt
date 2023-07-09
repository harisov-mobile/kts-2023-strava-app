package ru.internetcloud.strava.domain.common.util

fun Int?.orDefault(): Int = this ?: 0

fun Int.convertToString(): String {
    return if (this == 0) {
        ""
    } else {
        this.toString()
    }
}
