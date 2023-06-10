package ru.internetcloud.strava.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import ru.internetcloud.strava.R
import ru.internetcloud.strava.domain.common.model.Source
import ru.internetcloud.strava.domain.profile.model.Profile
import ru.internetcloud.strava.presentation.common.compose.ShowEmptyData
import ru.internetcloud.strava.presentation.common.compose.ShowError
import ru.internetcloud.strava.presentation.common.compose.ShowLoadingData
import ru.internetcloud.strava.presentation.common.compose.ShowSource
import ru.internetcloud.strava.presentation.common.compose.TopBarWithLogout
import ru.internetcloud.strava.presentation.util.UiState
import ru.internetcloud.strava.presentation.util.addLine
import ru.internetcloud.strava.presentation.util.addPartWithComma

@Composable
fun ShowProfileScreen() {
    val viewModel: ProfileViewModel = viewModel()
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopBarWithLogout(title = stringResource(id = R.string.navigation_item_you))
        }
    ) { paddingContent ->
        Box(modifier = Modifier.padding(paddingContent)) {
            when (screenState) {
                is UiState.Error -> {
                    ShowError(
                        message = stringResource(id = R.string.strava_server_unavailable)
                            .addLine((screenState as UiState.Error).exception.message.toString()),
                        onTryAgainClick = viewModel::fetchProfile
                    )
                }
                UiState.Loading -> {
                    ShowLoadingData()
                }
                is UiState.Success -> {
                    ShowProfile(
                        profile = (screenState as UiState.Success<Profile>).data,
                        source = (screenState as UiState.Success<Profile>).source
                    )
                }
                UiState.EmptyData -> {
                    ShowEmptyData(message = stringResource(id = R.string.no_data))
                }
            }
        }
    }
}

@Composable
private fun ShowProfile(
    profile: Profile,
    source: Source
) {
    Column {
        ShowSource(source)
        Column(
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
                .padding(16.dp)
        ) {
            Row {
                AsyncImage(
                    modifier = Modifier
                        .size(124.dp)
                        .clip(CircleShape),
                    model = profile.imageUrl,
                    placeholder = painterResource(id = R.drawable.no_photo),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "${profile.firstName} ${profile.lastName}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = profile.city.addPartWithComma(profile.state).addPartWithComma(profile.country),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Row {
                Column {
                    Text(
                        text = stringResource(id = R.string.profile_following),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = profile.friendCount.toString(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = stringResource(id = R.string.profile_followers),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = profile.followerCount.toString(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}
