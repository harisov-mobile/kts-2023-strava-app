package ru.internetcloud.strava.domain.common.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class DateConverter {

    private val ERROR_DATE = "0001-01-01T00:00:00Z"
    private val OUTPUT_DATE_TIME_FORMAT_WITH_GMT = "dd.MM.yyyy HH:mm:ss aaa z"
    private val EMPTY_STRING_VALUE = ""

    private val DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
    private val DATE_TIME_FORMAT_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    private val DATE_FORMAT = "dd.MM.yyyy"
    private val TIME_FORMAT = "HH : mm"

    @SuppressLint("SimpleDateFormat")
    private val dateTimeSdf = SimpleDateFormat(DATE_TIME_FORMAT)

    @SuppressLint("SimpleDateFormat")
    private val dateTimeIso8601Sdf = SimpleDateFormat(DATE_TIME_FORMAT_ISO8601)

    @SuppressLint("SimpleDateFormat")
    private val dateSdf = SimpleDateFormat(DATE_FORMAT)

    @SuppressLint("SimpleDateFormat")
    private val timeSdf = SimpleDateFormat(TIME_FORMAT)

    @SuppressLint("SimpleDateFormat")
    private val dateTimeWithGmtSdf = SimpleDateFormat(OUTPUT_DATE_TIME_FORMAT_WITH_GMT)

    private val cal: Calendar = Calendar.getInstance()

    @SuppressLint("SimpleDateFormat")
    fun fromStringToDate(dateString: String): Date? {
        return dateTimeSdf.parse(dateString)
    }

    @SuppressLint("SimpleDateFormat")
    fun getDateISO8601String(date: Date?): String {
        return date?.let { dateTimeIso8601Sdf.format(date) } ?: ""
    }

    @SuppressLint("SimpleDateFormat")
    fun getDateString(date: Date?): String {
        return date?.let { dateSdf.format(date) } ?: ""
    }

    @SuppressLint("SimpleDateFormat")
    fun getTimeString(date: Date?): String {
        return date?.let { timeSdf.format(date) } ?: ""
    }

    fun getDate(year: Int, month: Int, day: Int, hour: Int, minute: Int): Date {
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month)
        cal.set(Calendar.DAY_OF_MONTH, day)
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.time
    }

    @SuppressLint("SimpleDateFormat")
    fun fromStringInIso8601ToDate(dateString: String): Date {
        return try {
            dateTimeIso8601Sdf.parse(dateString) as Date
        } catch (e: Exception) {
            dateTimeIso8601Sdf.parse(ERROR_DATE) as Date
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getDateTimeStringWithGMT(date: Date?): String {
        return date?.let { dateTimeWithGmtSdf.format(date) } ?: EMPTY_STRING_VALUE
    }
}
