package ru.internetcloud.strava.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.internetcloud.strava.R
import ru.internetcloud.strava.presentation.common.compose.ShowEmptyData
import ru.internetcloud.strava.presentation.common.compose.StravaTheme
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

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            StravaTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navigationState = rememberNavigationState()

    Scaffold(
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
        AppNavGraph(
            navHostController = navigationState.navHostController,
            trainingListScreenContent = {
                ShowTrainingListScreen(
                    paddingValues = paddingValues,
                    onTrainingClickListener = remember {
                        { currentId ->
                            navigationState.navigateToDetail(currentId)
                        }
                    }
                )
            },
            trainingDetailScreenContent = { currentTrainingId ->
                ShowTrainingDetailScreen(
                    paddingValues = paddingValues,
                    trainingId = currentTrainingId,
                    onBackPressed = remember {
                        {
                            navigationState.navHostController.popBackStack()
                        }
                    }
                )
            },
            groupsScreenContent = {
                ShowGroupsScreen(
                    paddingValues = paddingValues
                )
            },
            youScreenContent = {
                ShowProfileScreen(
                    paddingValues = paddingValues
                )
            }
        )
    }
}

@Composable
private fun ShowGroupsScreen(
    paddingValues: PaddingValues
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.navigation_item_groups))
                }
            )
        }
    ) { paddingContent ->
        Box(modifier = Modifier.padding(paddingContent)) {
            ShowEmptyData(
                modifier = Modifier.padding(paddingValues),
                message = stringResource(id = R.string.groups_under_constraction)
            )
        }
    }
}
