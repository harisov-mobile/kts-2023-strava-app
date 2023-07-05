package ru.internetcloud.strava.data.common

interface ErrorMessageConverter {
    fun getMessageToException(exception: Exception): String

    fun getMessageToHTTPCode(code: Int): String
}
