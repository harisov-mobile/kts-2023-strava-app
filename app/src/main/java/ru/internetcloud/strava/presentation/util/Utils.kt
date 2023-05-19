package ru.internetcloud.strava.presentation.util

inline fun <T> T.applyIf(predicate: Boolean, block: T.() -> T): T {
    return if (predicate) block() else this
}
