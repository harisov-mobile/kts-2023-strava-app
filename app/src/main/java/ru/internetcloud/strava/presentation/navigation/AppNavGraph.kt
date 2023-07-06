package ru.internetcloud.strava.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    youScreenContent: @Composable () -> Unit,
    trainingListScreenContent: @Composable () -> Unit,
    trainingDetailScreenContent: @Composable (id: Long) -> Unit,
    trainingDetailEditScreenContent: @Composable (id: Long) -> Unit,
    trainingDetailAddScreenContent: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = Screen.Home.route
    ) {
        homeScreenNavGraph(
            trainingListScreenContent = trainingListScreenContent,
            trainingDetailScreenContent = trainingDetailScreenContent,
            trainingDetailEditScreenContent = trainingDetailEditScreenContent,
            trainingDetailAddScreenContent = trainingDetailAddScreenContent
        )

        composable(Screen.You.route) {
            youScreenContent()
        }
    }
}
