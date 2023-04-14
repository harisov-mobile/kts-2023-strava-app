package ru.internetcloud.strava.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    groupsScreenContent: @Composable () -> Unit,
    youScreenContent: @Composable () -> Unit,
    trainingListScreenContent: @Composable () -> Unit,
    trainingDetailScreenContent: @Composable (id: Long) -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Home.route
    ) {
        homeScreenNavGraph(
            trainingListScreenContent = trainingListScreenContent,
            trainingDetailScreenContent = trainingDetailScreenContent
        )

        composable(Screen.Groups.route) {
            groupsScreenContent()
        }
        composable(Screen.You.route) {
            youScreenContent()
        }
    }
}
