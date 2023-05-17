package ru.internetcloud.strava.presentation.util

fun String.addLine(line: String): String {
    return if (this.isEmpty()) {
        line
    } else {
        "$this\n$line"
    }
}

fun String.addPartWithComma(part: String): String {
    return if (part.isEmpty()) {
        this
    } else {
        "$this, $part"
    }
}
