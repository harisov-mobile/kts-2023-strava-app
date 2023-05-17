package ru.internetcloud.strava.presentation.main.composable

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.internetcloud.strava.presentation.navigation.AppNavGraph
import ru.internetcloud.strava.presentation.navigation.NavigationItem
import ru.internetcloud.strava.presentation.navigation.rememberNavigationState
import ru.internetcloud.strava.presentation.profile.ShowProfileScreen
import ru.internetcloud.strava.presentation.training.detail.ShowTrainingDetailScreen
import ru.internetcloud.strava.presentation.training.list.ShowTrainingListScreen

private val navItemList = listOf(
    NavigationItem.Home,
    NavigationItem.Groups,
    NavigationItem.You
)

@Composable
fun MainScreen() {
    val navigationState = rememberNavigationState()

    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navigationState.navHostController.currentBackStackEntryAsState()

                val items = navItemList

                items.forEach { item ->
                    val selected = navBackStackEntry?.destination?.hierarchy?.any {
                        it.route == item.screen.route
                    } ?: false
                    BottomNavigationItem(
                        selected = selected,
                        onClick = {
                            if (!selected) {
                                navigationState.navigateTo(item.screen.route)
                            }
                        },
                        icon = {
                            Icon(item.icon, contentDescription = null)
                        },
                        label = {
                            Text(text = stringResource(id = item.titleResId))
                        },
                        selectedContentColor = MaterialTheme.colors.onPrimary,
                        unselectedContentColor = MaterialTheme.colors.onSecondary
                    )
                }
            }
        }
    ) { paddingValues ->
        AppNavGraph(
            navHostController = navigationState.navHostController,
            trainingListScreenContent = {
                ShowTrainingListScreen(
                    paddingValues = paddingValues,
                    onTrainingClickListener = remember {
                        { currentId ->
                            navigationState.navigateToDetail(currentId)
                        }
                    }
                )
            },
            trainingDetailScreenContent = { currentTrainingId ->
                ShowTrainingDetailScreen(
                    paddingValues = paddingValues,
                    trainingId = currentTrainingId,
                    onBackPressed = remember {
                        {
                            navigationState.navHostController.popBackStack()
                        }
                    }
                )
            },
            groupsScreenContent = {
                ShowGroupsScreen(
                    paddingValues = paddingValues
                )
            },
            youScreenContent = {
                ShowProfileScreen(
                    paddingValues = paddingValues
                )
            }
        )
    }
}
