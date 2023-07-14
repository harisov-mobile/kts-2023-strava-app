package ru.internetcloud.strava.presentation.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    youScreenContent: @Composable () -> Unit,
    trainingListScreenContent: @Composable () -> Unit,
    trainingDetailScreenContent: @Composable (id: Long) -> Unit,
    trainingDetailEditScreenContent: @Composable (id: Long) -> Unit,
    trainingDetailAddScreenContent: @Composable () -> Unit,
    webScreenContent: @Composable (link: String) -> Unit,
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

        composable(
            route = Screen.Web.route,
            arguments = listOf(
                navArgument(Screen.KEY_LINK) {
                    type = NavType.StringType
                }
            )
        ) { navBackStackEntry ->
            val link = navBackStackEntry.arguments?.getString(Screen.KEY_LINK)
            link?.let { currentLink ->
                webScreenContent(link = currentLink)
            }
        }

        composable(
            route = Screen.WebDeepLink.route,
            deepLinks = listOf(navDeepLink {
                uriPattern = "https://internetcloud.ru"
                action = Intent.ACTION_VIEW
            })
        ) {
            val currentLink =  "https://internetcloud.ru/sites/gllacy/index.html"
            webScreenContent(link = currentLink)
        }
    }
}
