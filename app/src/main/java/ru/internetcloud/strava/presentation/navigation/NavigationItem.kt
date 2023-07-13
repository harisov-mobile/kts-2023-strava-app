package ru.internetcloud.strava.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Web
import androidx.compose.ui.graphics.vector.ImageVector
import ru.internetcloud.strava.R

sealed class NavigationItem(
    val screen: Screen,
    val titleResId: Int,
    val icon: ImageVector
) {

    object Home : NavigationItem(
        screen = Screen.Home,
        titleResId = R.string.navigation_item_home,
        icon = Icons.Outlined.Home
    )

    object Web : NavigationItem(
        screen = Screen.Web,
        titleResId = R.string.navigation_item_home,
        icon = Icons.Outlined.Web
    )
    object You : NavigationItem(
        screen = Screen.You,
        titleResId = R.string.navigation_item_you,
        icon = Icons.Outlined.Person
    )
}
