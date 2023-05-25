package ru.internetcloud.strava.presentation.main.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.internetcloud.strava.R
import ru.internetcloud.strava.presentation.common.compose.ShowEmptyData
import ru.internetcloud.strava.presentation.common.compose.TopBarWithLogout

@Composable
fun ShowGroupsScreen() {
    Scaffold(
        topBar = {
            TopBarWithLogout(title = stringResource(id = R.string.navigation_item_groups))
        }
    ) { paddingContent ->
        Box(modifier = Modifier.padding(paddingContent)) {
            ShowEmptyData(
                message = stringResource(id = R.string.groups_under_constraction)
            )
        }
    }
}
