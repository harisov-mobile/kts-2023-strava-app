package ru.internetcloud.strava.domain.common.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

object DateConverter {

    private const val DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
    private const val DATE_TIME_FORMAT_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    private const val DATE_FORMAT = "dd.MM.yyyy"
    private const val TIME_FORMAT = "HH : mm"

    @SuppressLint("SimpleDateFormat")
    fun fromStringToDate(dateString: String): Date? {
        return SimpleDateFormat(DATE_TIME_FORMAT).parse(dateString)
    }

    @SuppressLint("SimpleDateFormat")
    fun getDateISO8601String(date: Date?): String {
        val sdf = SimpleDateFormat(DATE_TIME_FORMAT_ISO8601)
        val ddd = date?.let { sdf.format(date) } ?: ""
        return ddd
    }

    @SuppressLint("SimpleDateFormat")
    fun getDateString(date: Date?): String {
        val sdf = SimpleDateFormat(DATE_FORMAT)
        return date?.let { sdf.format(date) } ?: ""
    }

    @SuppressLint("SimpleDateFormat")
    fun getTimeString(date: Date?): String {
        val sdf = SimpleDateFormat(TIME_FORMAT)
        return date?.let { sdf.format(date) } ?: ""
    }

    fun getDate(year: Int, month: Int, day: Int, hour: Int, minute: Int): Date {
        val cal: Calendar = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month)
        cal.set(Calendar.DAY_OF_MONTH, day)
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.time
    }
}
