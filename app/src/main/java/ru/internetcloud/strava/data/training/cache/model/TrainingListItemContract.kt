package ru.internetcloud.strava.data.training.cache.model

object TrainingListItemContract {
    const val TABLE_NAME = "training_list_items"

    object Columns {
        const val ID = "id"
        const val NAME = "name"
        const val DISTANCE = "distance"
        const val MOVING_TIME = "moving_time"
        const val SPORT = "sport"
        const val START_DATE = "start_date"
    }
}
