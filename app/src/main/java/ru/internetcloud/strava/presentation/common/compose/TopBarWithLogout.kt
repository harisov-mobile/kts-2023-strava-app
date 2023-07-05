package ru.internetcloud.strava.presentation.common.compose

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.inject
import ru.internetcloud.strava.presentation.logout.LogoutClickHelper

@Composable
fun TopBarWithLogout(
    title: String
) {
    val scope = rememberCoroutineScope()
    val logoutClickHelper: LogoutClickHelper by inject()

    TopAppBar(
        title = {
            Text(text = title)
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
