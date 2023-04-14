package ru.internetcloud.strava.presentation.util

fun String.addLine(line: String): String {
    if (this.isEmpty()) {
        return line
    } else {
        return "$this\n$line"
    }
}

fun String.addPartWithComma(part: String): String {
    if (part.isEmpty()) {
        return this
    } else {
        return "$this, $part"
    }
}
