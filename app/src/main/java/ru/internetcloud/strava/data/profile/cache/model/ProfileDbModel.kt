package ru.internetcloud.strava.data.profile.cache.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
data class ProfileDbModel(
    @PrimaryKey
    val id: Long,
    val firstName: String,
    val lastName: String,
    val city: String,
    val state: String,
    val country: String,
    val sex: String,
    val imageUrlMedium: String,
    val imageUrl: String,
    val friendCount: Int,
    val followerCount: Int
)
