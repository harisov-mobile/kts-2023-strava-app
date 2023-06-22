package ru.internetcloud.strava.data.training.cache.model

object TrainingContract {
    const val TABLE_NAME = "trainings"

    object Columns {
        const val ID = "id"
        const val NAME = "name"
        const val DISTANCE = "distance"
        const val MOVING_TIME = "moving_time"
        const val ELAPSED_TIME = "elapsed_time"
        const val TYPE = "type"
        const val SPORT_TYPE = "sport_type"
        const val START_DATE = "start_date"
        const val DESCRIPTION = "description"
        const val TRAINER = "trainer"
        const val COMMUTE = "commute"
    }
}
