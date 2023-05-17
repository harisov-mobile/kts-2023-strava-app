package ru.internetcloud.strava.presentation.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

class DateTimeConverter {

    companion object {

        private const val INPUT_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'" // "yyyy-MM-dd HH:mm:ss"
        private const val ERROR_DATE = "0001-01-01T00:00:00Z"
        private const val OUTPUT_DATE_TIME_FORMAT_WITH_GMT = "dd.MM.yyyy HH:mm:ss aaa z"
        private const val EMPTY_STRING_VALUE = ""

        @SuppressLint("SimpleDateFormat")
        fun fromStringToDate(dateString: String): Date {
            val date: Date
            date = try {
                SimpleDateFormat(INPUT_DATE_TIME_FORMAT).parse(dateString) as Date
            } catch (e: Exception) {
                SimpleDateFormat(INPUT_DATE_TIME_FORMAT).parse(ERROR_DATE) as Date
            }
            return date
        }

        @SuppressLint("SimpleDateFormat")
        fun getDateTimeStringWithGMT(date: Date?): String {
            val sdf = SimpleDateFormat(OUTPUT_DATE_TIME_FORMAT_WITH_GMT)
            return date?.let { sdf.format(date) } ?: EMPTY_STRING_VALUE
        }
    }
}
