package ru.internetcloud.strava.presentation.oldmain.model

data class SimpleItem(
    override val id: Int,
    val title: String,
    val author: String,
    val likesCount: Int
) : Item
