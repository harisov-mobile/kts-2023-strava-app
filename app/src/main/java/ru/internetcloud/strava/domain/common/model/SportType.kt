package ru.internetcloud.strava.domain.common.model

import androidx.annotation.StringRes
import ru.internetcloud.strava.R

enum class SportType(@StringRes val label: Int) {
    FootSports(R.string.sport_type_foot_sports),
    CycleSports(R.string.sport_type_cycle_sports),
    WaterSports(R.string.sport_type_water_sports),
    WinterSports(R.string.sport_type_winter_sports),
    OtherSports(R.string.sport_type_other_sports)
}

