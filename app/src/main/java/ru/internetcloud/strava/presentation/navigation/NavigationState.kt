package ru.internetcloud.strava.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

class NavigationState(val navHostController: NavHostController) {

    fun navigateTo(route: String) {
        navHostController.navigate(route) {
            popUpTo(navHostController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToDetail(id: Long) {
        navHostController.navigate(Screen.TrainingDetail.getRouteWithArg(id))
    }

    fun navigateToDetailEdit(id: Long) {
        navHostController.popBackStack()
        navHostController.navigate(Screen.TrainingDetailEdit.getRouteWithArg(id))
    }

    fun navigateToDetailWithPopUp(id: Long) {
        navHostController.popBackStack()
        navHostController.navigate(Screen.TrainingDetail.getRouteWithArg(id))
    }

    fun navigateToRoute(route: String, popUpTo: Boolean, popUpToInclusive: Boolean) {
        navHostController.navigate(route) {
            if (popUpTo) {
                popUpTo(route) {
                    inclusive = popUpToInclusive
                    saveState = false
                }
            }
        }
    }
}

@Composable
fun rememberNavigationState(
    navHostController: NavHostController = rememberNavController()
): NavigationState {
    return remember {
        NavigationState(navHostController)
    }
}
