package ru.internetcloud.strava.data.profile.cache.model

object ProfileContract {
    const val TABLE_NAME = "profile"

    object Columns {
        const val ID = "id"
        const val FIRST_NAME = "first_name"
        const val LAST_NAME = "last_name"
        const val CITY = "city"
        const val STATE = "state"
        const val COUNTRY = "country"
        const val SEX = "sex"
        const val IMAGE_URL_MEDIUM = "image_url_medium"
        const val IMAGE_URL = "image_url"
        const val FRIEND_COUNT = "friend_count"
        const val FOLLOWER_COUNT = "followerCount"
        const val WEIGHT = "weight"
    }
}
