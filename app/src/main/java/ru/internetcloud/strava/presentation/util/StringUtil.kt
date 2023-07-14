package ru.internetcloud.strava.presentation.util

import android.net.Uri

fun String.addLine(line: String): String {
    return if (this.isEmpty()) {
        line
    } else {
        "$this\n$line"
    }
}

fun String.addPartWithComma(part: String): String {
    return if (part.isEmpty()) {
        this
    } else {
        "$this, $part"
    }
}

fun String.replaceForUrl(): String {
    return this
        .replace("%".toRegex(), "%25") // Процент
        .replace(" ".toRegex(), "%20") // Пробел
        .replace("\t".toRegex(), "%20") // Табуляция (заменяем на пробел)
        .replace("\n".toRegex(), "%20") // Переход строки (заменяем на пробел)
        .replace("\r".toRegex(), "%20") // Возврат каретки (заменяем на пробел)
        .replace("!".toRegex(), "%21") // Восклицательный знак
        .replace("\"".toRegex(), "%22") // Двойная кавычка
        .replace("#".toRegex(), "%23") // Октоторп, решетка
        .replace("\\$".toRegex(), "%24") // Знак доллара
        .replace("&".toRegex(), "%26") // Амперсанд
        .replace("'".toRegex(), "%27") // Одиночная кавычка
        .replace("\\(".toRegex(), "%28") // Открывающаяся скобка
        .replace("\\)".toRegex(), "%29") // Закрывающаяся скобка
        .replace("\\*".toRegex(), "%2a") // Звездочка
        .replace("\\+".toRegex(), "%2b") // Знак плюс
        .replace(",".toRegex(), "%2c") // Запятая
        .replace("-".toRegex(), "%2d") // Дефис
        .replace("\\.".toRegex(), "%2e") // Точка
        .replace("/".toRegex(), "%2f") // Слеш, косая черта
        .replace(":".toRegex(), "%3a") // Двоеточие
        .replace(";".toRegex(), "%3b") // Точка с запятой
        .replace("<".toRegex(), "%3c") // Открывающаяся угловая скобка
        .replace("=".toRegex(), "%3d") // Знак равно
        .replace(">".toRegex(), "%3e") // Закрывающаяся угловая скобка
        .replace("\\?".toRegex(), "%3f") // Вопросительный знак
        .replace("@".toRegex(), "%40") // At sign, по цене, собачка
        .replace("\\[".toRegex(), "%5b") // Открывающаяся квадратная скобка
        .replace("\\\\".toRegex(), "%5c") // Одиночный обратный слеш '\'
        .replace("\\]".toRegex(), "%5d") // Закрывающаяся квадратная скобка
        .replace("\\^".toRegex(), "%5e") // Циркумфлекс
        .replace("_".toRegex(), "%5f") // Нижнее подчеркивание
        .replace("`".toRegex(), "%60") // Гравис
        .replace("\\{".toRegex(), "%7b") // Открывающаяся фигурная скобка
        .replace("\\|".toRegex(), "%7c") // Вертикальная черта
        .replace("\\}".toRegex(), "%7d") // Закрывающаяся фигурная скобка
        .replace("~".toRegex(), "%7e") // Тильда

}

fun String.encode(): String {
    return Uri.encode(this)
}
