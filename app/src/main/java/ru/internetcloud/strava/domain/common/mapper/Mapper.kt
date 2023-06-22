package ru.internetcloud.strava.domain.common.mapper

interface Mapper<From, To> {

    fun map(item: From): To

    fun map(list: List<From>): List<To> {
        return list.map { map(it) }
    }
}
