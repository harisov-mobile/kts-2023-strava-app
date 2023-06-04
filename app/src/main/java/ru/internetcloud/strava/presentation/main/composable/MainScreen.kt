package ru.internetcloud.strava.presentation.main.composable

import android.annotation.SuppressLint
import android.app.Application
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch
import ru.internetcloud.strava.R
import ru.internetcloud.strava.presentation.logout.LogoutDialog
import ru.internetcloud.strava.presentation.main.MainScreenEvent
import ru.internetcloud.strava.presentation.main.MainScreenViewModel
import ru.internetcloud.strava.presentation.main.MainScreenViewModelFactory
import ru.internetcloud.strava.presentation.navigation.AppNavGraph
import ru.internetcloud.strava.presentation.navigation.NavigationItem
import ru.internetcloud.strava.presentation.navigation.rememberNavigationState
import ru.internetcloud.strava.presentation.profile.ShowProfileScreen
import ru.internetcloud.strava.presentation.training.detail.ShowTrainingDetailScreen
import ru.internetcloud.strava.presentation.training.edit.EditMode
import ru.internetcloud.strava.presentation.training.edit.ShowTrainingEditScreen
import ru.internetcloud.strava.presentation.training.list.ShowTrainingListScreen

private val navItemList = listOf(
    NavigationItem.Home,
    NavigationItem.Groups,
    NavigationItem.You
)

private const val KEY_REFRESH = "key_refresh"

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MainScreen(
    app: Application,
    onNavigate: (Int, Bundle?) -> Unit
) {
    val navigationState = rememberNavigationState()

    val snackbarHostState = SnackbarHostState()
    val scope = rememberCoroutineScope()

    val viewModel: MainScreenViewModel = viewModel(factory = MainScreenViewModelFactory(app))

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
                                navigationState.navigateTo(item.screen.route)
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
                ShowTrainingListScreen(
                    currentBackStackEntry = navigationState.navHostController.currentBackStackEntry,
                    refreshKey = KEY_REFRESH,
                    onTrainingClickListener = navigationState::navigateToDetail,
                    onFABClickListener = navigationState::navigateToDetailAdd
                )
            },
            trainingDetailScreenContent = { currentTrainingId ->
                ShowTrainingDetailScreen(
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
                ShowTrainingEditScreen(
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
                ShowTrainingEditScreen(
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

            groupsScreenContent = {
                ShowGroupsScreen()
            },
            youScreenContent = {
                ShowProfileScreen()
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
