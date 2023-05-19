package ru.internetcloud.strava.presentation.main.composable

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch
import ru.internetcloud.strava.R
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

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MainScreen(app: Application) {
    val navigationState = rememberNavigationState()

    val snackbarHostState = SnackbarHostState()
    val scope = rememberCoroutineScope()

    val viewModel: MainScreenViewModel = viewModel(factory = MainScreenViewModelFactory(app))

    val internetConnectionAvailable = viewModel.internetConnectionAvailable.collectAsStateWithLifecycle(
        initialValue = true
    )
    val noInternetConnectionMessage = stringResource(id = R.string.no_internet_connection)

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
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
        when (internetConnectionAvailable.value) {
            true -> {
                snackbarHostState.currentSnackbarData?.dismiss()
            }
            false -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = noInternetConnectionMessage,
                        duration = SnackbarDuration.Indefinite
                    )
                }
            }
        }
        AppNavGraph(
            modifier = Modifier.padding(paddingValues),
            navHostController = navigationState.navHostController,
            trainingListScreenContent = {
                ShowTrainingListScreen(onTrainingClickListener = navigationState::navigateToDetail)
            },
            trainingDetailScreenContent = { currentTrainingId ->
                ShowTrainingDetailScreen(
                    trainingId = currentTrainingId,
                    onBackPressed = navigationState.navHostController::popBackStack
                )
            },
            groupsScreenContent = {
                ShowGroupsScreen()
            },
            youScreenContent = {
                ShowProfileScreen()
            }
        )
    }
}
