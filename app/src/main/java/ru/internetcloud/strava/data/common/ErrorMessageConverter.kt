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
        val message = if (code == 400) {
            applicaton.getString(R.string.bad_request_error)
        } else if (code == 401) {
            applicaton.getString(R.string.unauthorized_error)
        } else if (code == 403) {
            applicaton.getString(R.string.forbidden_error)
        } else if (code == 500) {
            applicaton.getString(R.string.internal_server_error)
        } else {
            applicaton.getString(R.string.unknown_network_error)
        }
        return message.addLine(applicaton.getString(R.string.http_error_code, code))
    }
}
