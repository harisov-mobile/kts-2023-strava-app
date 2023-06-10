package ru.internetcloud.strava.data.profile.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = ProfileContract.TABLE_NAME)
data class LocalProfile(
    @PrimaryKey
    @ColumnInfo(name = ProfileContract.Columns.ID)
    val id: Long,

    @ColumnInfo(name = ProfileContract.Columns.FIRST_NAME)
    val firstName: String,

    @ColumnInfo(name = ProfileContract.Columns.LAST_NAME)
    val lastName: String,

    @ColumnInfo(name = ProfileContract.Columns.CITY)
    val city: String,

    @ColumnInfo(name = ProfileContract.Columns.STATE)
    val state: String,

    @ColumnInfo(name = ProfileContract.Columns.COUNTRY)
    val country: String,

    @ColumnInfo(name = ProfileContract.Columns.SEX)
    val sex: String,

    @ColumnInfo(name = ProfileContract.Columns.IMAGE_URL_MEDIUM)
    val imageUrlMedium: String,

    @ColumnInfo(name = ProfileContract.Columns.IMAGE_URL)
    val imageUrl: String,

    @ColumnInfo(name = ProfileContract.Columns.FRIEND_COUNT)
    val friendCount: Int,

    @ColumnInfo(name = ProfileContract.Columns.FOLLOWER_COUNT)
    val followerCount: Int
)
