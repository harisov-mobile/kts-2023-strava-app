package ru.internetcloud.strava.presentation.util

import java.text.SimpleDateFormat
import java.util.Date

class DateTimeConverter {

    companion object {

        private const val INPUT_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'" // "yyyy-MM-dd HH:mm:ss"
        private const val ERROR_DATE = "0001-01-01T00:00:00Z"
        private const val DATE_FORMAT = "dd.MM.yyyy"
        private const val TIME_FORMAT = "HH : mm"
        private const val OUTPUT_DATE_TIME_FORMAT_WITH_GMT = "dd.MM.yyyy HH:mm:ss aaa z"

        fun fromStringToDate(dateString: String): Date {
            var date: Date
            try {
                date = SimpleDateFormat(INPUT_DATE_TIME_FORMAT).parse(dateString)
            } catch (e: Exception) {
                date = SimpleDateFormat(INPUT_DATE_TIME_FORMAT).parse(ERROR_DATE)
            }
            return date
        }

        fun getDateString(date: Date?): String {
            val sdf = SimpleDateFormat(DATE_FORMAT)
            return date?.let { sdf.format(date) } ?: ""
        }

        fun getTimeString(date: Date?): String {
            val sdf = SimpleDateFormat(TIME_FORMAT)
            return date?.let { sdf.format(date) } ?: ""
        }

        fun getDateTimeStringWithGMT(date: Date?): String {
            val sdf = SimpleDateFormat(OUTPUT_DATE_TIME_FORMAT_WITH_GMT)
            return date?.let { sdf.format(date) } ?: ""
        }
    }
}
