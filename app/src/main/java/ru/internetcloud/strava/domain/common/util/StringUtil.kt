package ru.internetcloud.strava.domain.common.util

fun String.toFloatOrDefault(): Float {
    return if (this.isEmpty()) {
        0f
    } else {
        try {
            this.toFloat()
        } catch (e: Exception) {
            0f
        }
    }
}
