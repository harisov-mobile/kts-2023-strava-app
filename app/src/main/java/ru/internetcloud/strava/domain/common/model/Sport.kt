package ru.internetcloud.strava.domain.common.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ru.internetcloud.strava.R

enum class Sport(@StringRes val label: Int, @DrawableRes val icon: Int, val sportType: SportType) {
    Run(R.string.sport_run, R.drawable.sports_run_normal_large, SportType.FootSports),
    TrailRun(R.string.sport_trail_run, R.drawable.sports_run_trail_normal_large, SportType.FootSports),
    Walk(R.string.sport_walk, R.drawable.sports_walk_normal_large, SportType.FootSports),
    Hike(R.string.sport_hike, R.drawable.sports_hike_normal_large, SportType.FootSports),
    Wheelchair(R.string.sport_wheelchair, R.drawable.sports_wheelchair_normal_large, SportType.FootSports),
    VirtualRun(R.string.sport_virtual_run, R.drawable.sports_run_normal_large, SportType.FootSports),

    Ride(R.string.sport_ride, R.drawable.sports_bike_normal_large, SportType.CycleSports),
    MountainBikeRide(R.string.sport_mountain_bike_ride, R.drawable.sports_bike_mountain_normal_large, SportType.CycleSports),
    GravelRide(R.string.sport_gravel_ride, R.drawable.sports_bike_gravel_normal_large, SportType.CycleSports),
    EBikeRide(R.string.sport_e_bike_ride, R.drawable.sports_e_bike_ride_normal_large, SportType.CycleSports),
    EMountainBikeRide(R.string.sport_e_mountain_bike_ride, R.drawable.sports_bike_e_mountain_normal_large, SportType.CycleSports),
    Handcycle(R.string.sport_handcycle, R.drawable.sports_handcycle_normal_large, SportType.CycleSports),
    Velomobile(R.string.sport_velomobile, R.drawable.sports_velomobile_normal_large, SportType.CycleSports),
    VirtualRide(R.string.sport_virtual_ride, R.drawable.sports_bike_normal_large, SportType.CycleSports),

    Swim(R.string.sport_swim, R.drawable.sports_water_normal_large, SportType.WaterSports),
    Surfing(R.string.sport_surfing, R.drawable.sports_surfing_normal_large, SportType.WaterSports),
    StandUpPaddling(R.string.sport_stand_up_paddling, R.drawable.sports_stand_up_paddling_normal_large, SportType.WaterSports),
    Windsurf(R.string.sport_stand_up_paddling, R.drawable.sports_windsurf_normal_large, SportType.WaterSports),
    Kitesurf(R.string.sport_kitesurf, R.drawable.sports_kitesurf_normal_large, SportType.WaterSports),
    Kayaking(R.string.sport_kayaking, R.drawable.sports_kayaking_normal_large, SportType.WaterSports),
    Rowing(R.string.sport_rowing, R.drawable.sports_rowing_normal_large, SportType.WaterSports),
    Canoeing(R.string.sport_canoeing, R.drawable.sports_kayaking_normal_large, SportType.WaterSports),
    Sail(R.string.sport_sail, R.drawable.sports_windsurf_normal_large, SportType.WaterSports),
    VirtualRow(R.string.sport_virtual_row, R.drawable.sports_virtual_row_normal_large, SportType.WaterSports),

    IceSkate(R.string.sport_ice_skate, R.drawable.sports_ice_skate_normal_large, SportType.WinterSports),
    AlpineSki(R.string.sport_alpine_ski, R.drawable.sports_ski_normal_large, SportType.WinterSports),
    NordicSki(R.string.sport_nordic_ski, R.drawable.sports_ski_normal_large, SportType.WinterSports),
    BackcountrySki(R.string.sport_backcountry_ski, R.drawable.sports_ski_normal_large, SportType.WinterSports),
    Snowboard(R.string.sport_snowboard, R.drawable.sports_snowboard_normal_large, SportType.WinterSports),
    Snowshoe(R.string.sport_snowshoe, R.drawable.sports_snowshoe_normal_large, SportType.WinterSports),

    InlineSkate(R.string.sport_inline_skate, R.drawable.sports_inline_skate_normal_large, SportType.OtherSports),
    RollerSki(R.string.sport_roller_ski, R.drawable.sports_ski_normal_large, SportType.OtherSports),
    Workout(R.string.sport_workout, R.drawable.sports_other_normal_large, SportType.OtherSports), // икнока
    RockClimbing(R.string.sport_rock_climbing, R.drawable.sports_rock_climbing_normal_large, SportType.OtherSports),
    WeightTraining(R.string.sport_weight_training, R.drawable.sports_weight_training_normal_large, SportType.OtherSports),
    Elliptical(R.string.sport_elliptical, R.drawable.sports_other_normal_large, SportType.OtherSports),
    StairStepper(R.string.sport_stair_stepper, R.drawable.sports_other_normal_large, SportType.OtherSports),
    Crossfit(R.string.sport_crossfit, R.drawable.sports_other_normal_large, SportType.OtherSports),
    Yoga(R.string.sport_yoga, R.drawable.sports_yoga_normal_large, SportType.OtherSports),
    Skateboard(R.string.sport_skateboard, R.drawable.sports_skateboard_normal_large, SportType.OtherSports),
    Soccer(R.string.sport_soccer, R.drawable.sports_soccer_normal_large, SportType.OtherSports),
    Golf(R.string.sport_golf, R.drawable.sports_golf_large, SportType.OtherSports),
    Tennis(R.string.sport_tennis, R.drawable.sports_tennis_normal_large, SportType.OtherSports),
    Pickleball(R.string.sport_pickleball, R.drawable.sports_pickleball_normal_large, SportType.OtherSports),
    Racquetball(R.string.sport_racquetball, R.drawable.sports_racquetball_normal_large, SportType.OtherSports),
    Squash(R.string.sport_squash, R.drawable.sports_squash_normal_large, SportType.OtherSports),
    Badminton(R.string.sport_badminton, R.drawable.sports_badminton_normal_large, SportType.OtherSports),
    TableTennis(R.string.sport_table_tennis, R.drawable.sports_table_tennis_normal_large, SportType.OtherSports),
    HighIntensityIntervalTraining(R.string.sport_hiit, R.drawable.sports_hiit_normal_large, SportType.OtherSports),
    Pilates(R.string.sport_pilates, R.drawable.sports_pilates_normal_large, SportType.OtherSports)
}

fun getSportsByType(sportType: SportType): List<Sport> {
    return Sport.values().toList().filter { sport -> sportType == sport.sportType }
}

fun getSportByName(name: String): Sport {
    return Sport.values().find { sport -> name.equals(sport.toString()) } ?: error("$name is not found in Sport")
}

fun getSportsWithSportType(): List<Any> {
    val list = mutableListOf<Any>()
    SportType.values().forEach { sportType ->
        list.add(sportType)
        getSportsByType(sportType).forEach { sport ->
            list.add(sport)
        }
    }
    return list.toList()
}
