package ru.internetcloud.strava.presentation.common.compose

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.inject
import ru.internetcloud.strava.presentation.logout.LogoutClickHelper

@Composable
fun TopAppBarWithNavigationIconAndLogout(
    title: String,
    onArrowBackPressed: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val logoutClickHelper: LogoutClickHelper by inject()

    TopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    onArrowBackPressed()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(
                onClick = remember {
                    {
                        scope.launch {
                            logoutClickHelper.onLogoutClick()
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = null
                )
            }
        }
    )
}
