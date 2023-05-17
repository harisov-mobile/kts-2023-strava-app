package ru.internetcloud.strava.presentation.main.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.internetcloud.strava.R
import ru.internetcloud.strava.presentation.common.compose.ShowEmptyData

@Composable
fun ShowGroupsScreen(
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
