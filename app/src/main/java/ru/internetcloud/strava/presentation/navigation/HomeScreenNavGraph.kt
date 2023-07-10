package ru.internetcloud.strava.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import ru.internetcloud.strava.domain.common.util.orDefault

fun NavGraphBuilder.homeScreenNavGraph(
    trainingListScreenContent: @Composable () -> Unit,
    trainingDetailScreenContent: @Composable (id: Long) -> Unit,
    trainingDetailEditScreenContent: @Composable (id: Long) -> Unit,
    trainingDetailAddScreenContent: @Composable () -> Unit
) {
    navigation(
        startDestination = Screen.TrainingList.route,
        route = Screen.Home.route
    ) {
        composable(Screen.TrainingList.route) {
            trainingListScreenContent()
        }

        composable(
            route = Screen.TrainingDetail.route,
            arguments = listOf(
                navArgument(Screen.KEY_ID) {
                    type = NavType.LongType
                }
            )
        ) { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getLong("id").orDefault()
            trainingDetailScreenContent(id = id)
        }

        composable(
            route = Screen.TrainingDetailEdit.route,
            arguments = listOf(
                navArgument(Screen.KEY_ID) {
                    type = NavType.LongType
                }
            )
        ) { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getLong(Screen.KEY_ID).orDefault()
            trainingDetailEditScreenContent(id = id)
        }

        composable(Screen.TrainingDetailAdd.route) {
            trainingDetailAddScreenContent()
        }
    }
}
