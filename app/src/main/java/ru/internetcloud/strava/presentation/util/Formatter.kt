package ru.internetcloud.strava.presentation.util

class Formatter {

    companion object {

        private const val SECONDS_IN_HOUR = 3600
        private const val SECONDS_IN_MINUTE = 60

        fun getDetailedTime(movingTime: Int, formatHoursMinutes: String, formatMinutesSeconds: String): String {
            return if (movingTime >= SECONDS_IN_HOUR) {
                val hours: Int = movingTime / SECONDS_IN_HOUR
                val minutes: Int = (movingTime - hours * SECONDS_IN_HOUR) / SECONDS_IN_MINUTE
                String.format(
                    formatHoursMinutes,
                    hours,
                    minutes
                )
            } else {
                val minutes: Int = movingTime / SECONDS_IN_MINUTE
                val seconds: Int = movingTime - minutes * SECONDS_IN_MINUTE
                String.format(
                    formatMinutesSeconds,
                    minutes,
                    seconds
                )
            }
        }

        fun getFormattedValue(value: Double, format: String): String {
            return String.format(format, value)
        }

        fun getHours(movingTime: Int): Int = movingTime / SECONDS_IN_HOUR

        fun getMinutes(movingTime: Int) = (movingTime - getHours(movingTime) * SECONDS_IN_HOUR) / SECONDS_IN_MINUTE

        fun getSeconds(movingTime: Int) =
            movingTime - getHours(movingTime) * SECONDS_IN_HOUR - getMinutes(movingTime) * SECONDS_IN_MINUTE

        fun getTimeInSeconds(hours: Int, minutes: Int, seconds: Int): Int {
            return hours * SECONDS_IN_HOUR + minutes * SECONDS_IN_MINUTE + seconds
        }
    }
}
