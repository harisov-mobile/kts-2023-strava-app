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
        navHostController.navigate(Screen.TrainingDetailEdit.getRouteWithArg(id))
    }

    fun navigateToDetailAdd() {
        navHostController.navigate(Screen.TrainingDetailAdd.route)
    }

    fun navigateToDetailWithPopBackStack(id: Long) {
        navHostController.popBackStack()
        navHostController.navigate(Screen.TrainingDetail.getRouteWithArg(id))
    }

    fun navigateBackWithRefresh(refreshKey: String, refresh: Boolean) {
        navHostController.previousBackStackEntry?.savedStateHandle?.set(refreshKey, refresh)
        navHostController.popBackStack()
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

    fun navigateToWeb(link: String) {
        navHostController.navigate(Screen.Web.getRouteWithArg(link))
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
