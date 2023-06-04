package ru.internetcloud.strava.data.common

import android.app.Application
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import ru.internetcloud.strava.R
import ru.internetcloud.strava.presentation.util.addLine

object ErrorMessageConverter {

    private lateinit var applicaton: Application

    fun init(applicaton: Application) {
        this.applicaton = applicaton
    }

    fun getMessageToException(exception: Exception): String {
        return if (exception is SocketTimeoutException || exception is UnknownHostException) {
            applicaton.getString(R.string.no_internet_connection).addLine(exception.message.toString())
        } else {
            applicaton.getString(R.string.unknown_network_error).addLine(exception.message.toString())
        }
    }

    fun getMessageToHTTPCode(code: Int): String {
        val messageResId = when (code) {
            400 -> R.string.bad_request_error
            401 -> R.string.unauthorized_error
            403 -> R.string.forbidden_error
            500 -> R.string.internal_server_error
            else -> R.string.unknown_network_error
        }
        return applicaton.getString(messageResId).addLine(applicaton.getString(R.string.http_error_code, code))
    }
}
