package ru.internetcloud.strava.domain.common.util

import java.util.Date

interface DateConverter {
    fun fromStringToDate(dateString: String): Date?

    fun getDateISO8601String(date: Date?): String

    fun getDateString(date: Date?): String

    fun getTimeString(date: Date?): String

    fun getDate(year: Int, month: Int, day: Int, hour: Int, minute: Int): Date

    fun fromStringInIso8601ToDate(dateString: String): Date

    fun getDateTimeStringWithGMT(date: Date?): String
}
