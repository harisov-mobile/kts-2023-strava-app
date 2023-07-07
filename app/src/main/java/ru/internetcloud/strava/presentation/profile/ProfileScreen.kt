package ru.internetcloud.strava.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import org.koin.androidx.compose.viewModel
import ru.internetcloud.strava.R
import ru.internetcloud.strava.domain.common.model.Source
import ru.internetcloud.strava.domain.profile.model.Profile
import ru.internetcloud.strava.presentation.common.compose.ShowError
import ru.internetcloud.strava.presentation.common.compose.ShowLoadingData
import ru.internetcloud.strava.presentation.common.compose.ShowSource
import ru.internetcloud.strava.presentation.common.compose.TopBarWithLogout
import ru.internetcloud.strava.presentation.profile.model.UiProfileState
import ru.internetcloud.strava.presentation.util.addLine

@Composable
fun ProfileScreen() {
    val viewModel: ProfileViewModel by viewModel()
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val state = screenState

    Scaffold(
        topBar = {
            TopBarWithLogout(title = stringResource(id = R.string.navigation_item_you))
        }
    ) { paddingContent ->
        Box(modifier = Modifier.padding(paddingContent)) {
            when (state) {
                is UiProfileState.Error -> {
                    ShowError(
                        message = stringResource(id = R.string.strava_server_unavailable)
                            .addLine(state.exception.message.toString()),
                        onTryAgainClick = viewModel::fetchProfile
                    )
                }

                UiProfileState.Loading -> {
                    ShowLoadingData()
                }

                is UiProfileState.Success -> {
                    ShowProfile(
                        profile = state.profile,
                        source = state.source,
                        onSave = viewModel::saveProfile,
                        state = state,
                        onEvent = viewModel::handleEvent
                    )
                }
            }
        }
    }
}

@Composable
private fun ShowProfile(
    profile: Profile,
    source: Source,
    onSave: () -> Unit,
    onEvent: (EditProfileEvent) -> Unit,
    state: UiProfileState
) {
    val scrollState = rememberScrollState()

    Column {
        ShowSource(source)
        Column(
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
                .fillMaxSize()
                .verticalScroll(
                    state = scrollState
                )
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
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Row {
                Column {
                    Text(
                        text = stringResource(id = R.string.profile_following),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = profile.friendCount.toString(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.big_margin)))
                Column {
                    Text(
                        text = stringResource(id = R.string.profile_followers),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = profile.followerCount.toString(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
            Column {
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.big_margin)))

                OutlinedTextField(
                    readOnly = true,
                    value = profile.country,
                    onValueChange = { },
                    label = { Text(text = stringResource(id = R.string.profile_country)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.normal_margin)))

                OutlinedTextField(
                    readOnly = true,
                    value = profile.state,
                    onValueChange = { },
                    label = { Text(text = stringResource(id = R.string.profile_state)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.normal_margin)))

                OutlinedTextField(
                    readOnly = true,
                    value = profile.city,
                    onValueChange = { },
                    label = { Text(text = stringResource(id = R.string.profile_city)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.normal_margin)))

                OutlinedTextField(
                    readOnly = if (state is UiProfileState.Success) {
                            state.saving
                        } else false,
                    value = profile.weight.toString(),
                    onValueChange =  remember { { onEvent(EditProfileEvent.OnWeightChange(it.toFloat())) } },
                    label = { Text(text = stringResource(id = R.string.profile_weight)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.normal_margin)))

                Button(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    onClick = {
                        if (state is UiProfileState.Success) {
                            if (!state.saving) {
                                onSave()
                            }
                        }
                    }
                ) {
                    if (state is UiProfileState.Success) {
                        if (state.saving) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colors.onPrimary,
                                modifier = Modifier.padding(end = 16.dp)
                            )
                        }
                        Text(
                            text = stringResource(id = R.string.profile_save_button)
                        )
                    }
                }
            }
        }
    }
}


//@Preview
//@Composable
//fun ProfilePreview() {
//    ShowProfile(
//        profile = Profile(
//            id = 1,
//            firstName = "Rustam",
//            lastName = "Harisov",
//            city = "Tallin",
//            state = "Hollan",
//            country = "Country",
//            sex = "M",
//            imageUrlMedium = "",
//            imageUrl = "",
//            friendCount = 5,
//            followerCount = 2
//        ),
//        source = Source.RemoteApi
//    )
//}