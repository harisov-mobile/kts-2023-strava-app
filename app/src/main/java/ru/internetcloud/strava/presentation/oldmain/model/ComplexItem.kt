package ru.internetcloud.strava.presentation.oldmain.model

data class ComplexItem(
    override val id: Int,
    val title: String,
    val author: String,
    val image: String? = null
) : Item
