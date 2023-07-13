package ru.internetcloud.strava.presentation.main.composable

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.viewModel
import org.koin.core.parameter.parametersOf
import ru.internetcloud.strava.R
import ru.internetcloud.strava.presentation.logout.LogoutDialog
import ru.internetcloud.strava.presentation.main.MainScreenEvent
import ru.internetcloud.strava.presentation.main.MainScreenViewModel
import ru.internetcloud.strava.presentation.navigation.AppNavGraph
import ru.internetcloud.strava.presentation.navigation.NavigationItem
import ru.internetcloud.strava.presentation.navigation.Screen
import ru.internetcloud.strava.presentation.navigation.rememberNavigationState
import ru.internetcloud.strava.presentation.profile.ProfileScreen
import ru.internetcloud.strava.presentation.training.detail.TrainingDetailScreen
import ru.internetcloud.strava.presentation.training.edit.EditMode
import ru.internetcloud.strava.presentation.training.edit.TrainingEditScreen
import ru.internetcloud.strava.presentation.training.list.TrainingListScreen
import ru.internetcloud.strava.presentation.web.WebScreen

private val navItemList = listOf(
    NavigationItem.Home,
    NavigationItem.Web,
    NavigationItem.You
)

private const val KEY_REFRESH = "key_refresh"

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MainScreen(
    keyMessage: String,
    onNavigate: (Int, Bundle?) -> Unit
) {
    val navigationState = rememberNavigationState() // функция Андрея Сумина

    val snackbarHostState = SnackbarHostState()
    val scope = rememberCoroutineScope()

    val viewModel: MainScreenViewModel by viewModel {
        parametersOf(keyMessage)
    }

    val internetConnectionAvailable = viewModel.internetConnectionAvailable.collectAsStateWithLifecycle(
        initialValue = true
    )
    val noInternetConnectionMessage = stringResource(id = R.string.no_internet_connection)

    val showLogoutDialog = viewModel.showLogoutDialog.collectAsStateWithLifecycle(initialValue = false)

    val context = LocalContext.current

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
                        modifier = Modifier.background(MaterialTheme.colors.surface),
                        selected = selected,
                        onClick = {
                            if (!selected) {
                                val currentRoute = item.screen.route
                                when (currentRoute) {
                                    Screen.Web.route -> navigationState.navigateToWeb(link = "https://www.strava.com")
                                    Screen.Home.route -> navigationState.navigateToHomeItem()
                                    else -> navigationState.navigateTo(currentRoute)
                                }
                            }
                        },
                        icon = {
                            Icon(item.icon, contentDescription = null)
                        },
                        label = {
                            Text(text = stringResource(id = item.titleResId))
                        },
                        selectedContentColor = MaterialTheme.colors.primary,
                        unselectedContentColor = MaterialTheme.colors.secondaryVariant
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

        LogoutDialog(
            show = showLogoutDialog.value,
            onDismiss = viewModel::onLogoutDialogDismiss,
            onConfirm = viewModel::onLogoutDialogConfirm
        )

        AppNavGraph(
            modifier = Modifier.padding(paddingValues),
            navHostController = navigationState.navHostController,
            trainingListScreenContent = {
                TrainingListScreen(
                    currentBackStackEntry = navigationState.navHostController.currentBackStackEntry,
                    refreshKey = KEY_REFRESH,
                    onTrainingClick = navigationState::navigateToDetail,
                    onFloatingActionButtonClick = navigationState::navigateToDetailAdd
                )
            },
            trainingDetailScreenContent = { currentTrainingId ->
                TrainingDetailScreen(
                    trainingId = currentTrainingId,
                    currentBackStackEntry = navigationState.navHostController.currentBackStackEntry,
                    refreshKey = KEY_REFRESH,
                    onBackPressed = navigationState.navHostController::popBackStack,
                    onEditTraining = navigationState::navigateToDetailEdit,
                    onBackWithRefresh = {
                        navigationState.navigateBackWithRefresh(
                            refreshKey = KEY_REFRESH,
                            refresh = true
                        )
                    }
                )
            },
            trainingDetailEditScreenContent = { currentTrainingId ->
                TrainingEditScreen(
                    trainingId = currentTrainingId,
                    editMode = EditMode.Edit,
                    onReturn = navigationState::navigateToDetailWithPopBackStack,
                    onBackPressed = navigationState.navHostController::popBackStack,
                    onBackWithRefresh = {
                        navigationState.navigateBackWithRefresh(
                            refreshKey = KEY_REFRESH,
                            refresh = true
                        )
                    }
                )
            },

            trainingDetailAddScreenContent = {
                TrainingEditScreen(
                    trainingId = 0,
                    editMode = EditMode.Add,
                    onReturn = navigationState::navigateToDetailWithPopBackStack,
                    onBackPressed = navigationState.navHostController::popBackStack,
                    onBackWithRefresh = {
                        navigationState.navigateBackWithRefresh(
                            refreshKey = KEY_REFRESH,
                            refresh = true
                        )
                    }
                )
            },
            youScreenContent = {
                ProfileScreen()
            },

            webScreenContent = { link ->
                WebScreen(link)
            }
        )
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.screenEventFlow.collect { event ->
            when (event) {
                is MainScreenEvent.NavigateToLogout -> {
                    onNavigate(R.id.action_mainFragment_to_authFragment, event.args)
                }

                is MainScreenEvent.ShowMessage -> {
                    Toast.makeText(context, event.messageRes, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

