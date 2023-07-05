package ru.internetcloud.strava.domain.common.util

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

sealed class StringVs {
    data class StringResource(
        @StringRes val id: Int,
        val formatArgs: List<Any>
    ) : StringVs()

    data class StringPlural(
        @PluralsRes val id: Int,
        val quantity: Int,
        val formatArgs: List<Any>
    ) : StringVs()

    data class StringValue(val string: String) : StringVs()
}

fun String.toStringVs(): StringVs {
    return StringVs.StringValue(this)
}

fun Int.toStringVs(vararg formatArgs: Any): StringVs {
    return StringVs.StringResource(this, formatArgs.toList())
}

fun Int.toPluralStringVs(quantity: Int, vararg formatArgs: Any): StringVs {
    return StringVs.StringPlural(this, quantity, formatArgs.toList())
}

fun Context.parseStringVs(stringVs: StringVs): String {
    return when (stringVs) {
        is StringVs.StringResource -> getString(
            stringVs.id,
            *stringVs.formatArgs.toTypedArray()
        )
        is StringVs.StringPlural -> resources.getQuantityString(
            stringVs.id,
            stringVs.quantity,
            *stringVs.formatArgs.toTypedArray()
        )
        is StringVs.StringValue -> stringVs.string
    }
}
