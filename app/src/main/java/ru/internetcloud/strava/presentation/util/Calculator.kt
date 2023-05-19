package ru.internetcloud.strava.presentation.util

class Calculator {

    companion object {

        private const val METERS_IN_KILOMETER = 1000.0
        private const val KOEFF = 3.6

        fun calculateSpeed(distance: Float, movingTime: Int): Double {
            return if (movingTime == 0) {
                .0
            } else {
                distance / movingTime * KOEFF
            }
        }

        fun calculateDistance(distance: Float): Double {
            return distance / METERS_IN_KILOMETER
        }
    }
}
