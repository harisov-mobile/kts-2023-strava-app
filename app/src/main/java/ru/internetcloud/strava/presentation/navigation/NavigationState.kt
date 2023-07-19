package ru.internetcloud.strava.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ru.internetcloud.strava.presentation.util.encode

class NavigationState(val navHostController: NavHostController) {

    fun navigateTo(route: String) {
        navHostController.navigate(route) {
            val startDestination = navHostController.graph.findStartDestination()
            val parentStartDestination = startDestination.parent

            var maxEntry = navHostController.graph.findStartDestination().id

            navHostController.backQueue.forEachIndexed { index, navBackStackEntry ->
                if (navBackStackEntry.destination.parent == parentStartDestination) {
                    maxEntry = navBackStackEntry.destination.id
                }
            }

            popUpTo(maxEntry) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToHomeItem() {
        val startDestination = navHostController.graph.findStartDestination()
        val parentStartDestination = startDestination.parent

        var maxEntry = navHostController.graph.findStartDestination().id

        navHostController.backQueue.forEachIndexed { index, navBackStackEntry ->
            if (navBackStackEntry.destination.parent == parentStartDestination) {
                maxEntry = navBackStackEntry.destination.id
            }
        }

        navHostController.popBackStack(destinationId = maxEntry, inclusive = false, saveState = true)
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

    fun navigateToWebWithPopUp(link: String) {
        navHostController.navigate(Screen.Web.getRouteWithArg(link.encode())) {
            val startDestination = navHostController.graph.findStartDestination()
            val parentStartDestination = startDestination.parent

            var maxEntry = navHostController.graph.findStartDestination().id

            navHostController.backQueue.forEachIndexed { index, navBackStackEntry ->
                if (navBackStackEntry.destination.parent == parentStartDestination) {
                    maxEntry = navBackStackEntry.destination.id
                }
            }

            popUpTo(maxEntry) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToWeb(link: String) {
        navHostController.navigate(Screen.Web.getRouteWithArg(link.encode()))
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
