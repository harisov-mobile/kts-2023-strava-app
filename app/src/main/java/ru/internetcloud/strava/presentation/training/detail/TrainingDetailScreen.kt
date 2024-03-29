package ru.internetcloud.strava.presentation.training.detail

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import coil.compose.AsyncImage
import org.koin.androidx.compose.inject
import org.koin.androidx.compose.viewModel
import org.koin.core.parameter.parametersOf
import ru.internetcloud.strava.R
import ru.internetcloud.strava.domain.common.model.Source
import ru.internetcloud.strava.domain.common.model.getSportByName
import ru.internetcloud.strava.domain.common.util.DateConverter
import ru.internetcloud.strava.domain.common.util.parseStringVs
import ru.internetcloud.strava.domain.profile.model.Profile
import ru.internetcloud.strava.domain.training.model.Training
import ru.internetcloud.strava.domain.training.util.TrainingConverter
import ru.internetcloud.strava.presentation.common.compose.ShowError
import ru.internetcloud.strava.presentation.common.compose.ShowLoadingData
import ru.internetcloud.strava.presentation.common.compose.ShowSource
import ru.internetcloud.strava.presentation.common.theme.customTypography
import ru.internetcloud.strava.presentation.training.detail.model.UiTrainingDetailEvent
import ru.internetcloud.strava.presentation.training.detail.model.UiTrainingDetailState
import ru.internetcloud.strava.presentation.training.list.TimeDistanceSpeed
import ru.internetcloud.strava.presentation.util.addLine

@Composable
fun TrainingDetailScreen(
    trainingId: Long,
    currentBackStackEntry: NavBackStackEntry?,
    refreshKey: String,
    onBackPressed: () -> Unit,
    onBackWithRefresh: () -> Unit,
    onEditTraining: (id: Long) -> Unit
) {
    val viewModel: TrainingDetailViewModel by viewModel {
        parametersOf(trainingId)
    }

    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val state = screenState

    val showDropdownMenu = remember { mutableStateOf(false) }
    val context = LocalContext.current

    val shouldRefresh = currentBackStackEntry?.savedStateHandle?.remove<Boolean?>(refreshKey)
    shouldRefresh?.let { refresh ->
        if (refresh) {
            viewModel.fetchTraining(trainingId, isChanged = true)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (state is UiTrainingDetailState.Success) {
                            stringResource(
                                id = getSportByName(state.profileWithTraining.training.sport).label
                            )
                        } else {
                            stringResource(id = R.string.training_app_bar_title)
                        }
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if ((state is UiTrainingDetailState.Success &&
                                        state.isChanged) ||
                                (state is UiTrainingDetailState.Error &&
                                        state.isChanged) ||
                                (state is UiTrainingDetailState.Loading &&
                                        state.isChanged)
                            ) {
                                onBackWithRefresh()
                            } else {
                                onBackPressed()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    if (state is UiTrainingDetailState.Success) {
                        IconButton(onClick = { showDropdownMenu.value = !showDropdownMenu.value }) {
                            Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
                        }
                        DropdownMenu(
                            expanded = showDropdownMenu.value,
                            onDismissRequest = { showDropdownMenu.value = false }
                        ) {
                            DropdownMenuItem(onClick = {
                                onEditTraining(trainingId)
                            }) {
                                Icon(imageVector = Icons.Filled.Edit, contentDescription = null)
                                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.normal_margin)))
                                Text(text = stringResource(id = R.string.menu_edit))
                            }
                        }
                    }
                }
            )
        }
    ) { paddingContent ->
        Box(modifier = Modifier.padding(paddingContent)) {
            when (state) {
                is UiTrainingDetailState.Error -> {
                    ShowError(
                        message = stringResource(id = R.string.strava_server_unavailable)
                            .addLine(
                                state.exception.message.toString()
                            ),
                        onTryAgainClick = {
                            viewModel.fetchTraining(
                                id = trainingId,
                                isChanged = state.isChanged
                            )
                        }
                    )
                }

                is UiTrainingDetailState.Loading -> {
                    ShowLoadingData()
                }

                is UiTrainingDetailState.Success -> {
                    ShowTraining(
                        profile = state.profileWithTraining.profile,
                        training = state.profileWithTraining.training,
                        source = state.source
                    )
                }
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.screenEventFlow.collect { event ->
            when (event) {
                is UiTrainingDetailEvent.NavigateBack -> {
                    onBackWithRefresh()
                }

                is UiTrainingDetailEvent.ShowMessage -> {
                    Toast.makeText(
                        context,
                        context.parseStringVs(event.stringVs),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    BackHandler {
        if ((state is UiTrainingDetailState.Success &&
                    state.isChanged) ||
            (state is UiTrainingDetailState.Error &&
                    state.isChanged) ||
            (state is UiTrainingDetailState.Loading &&
                    state.isChanged)
        ) {
            onBackWithRefresh()
        } else {
            onBackPressed()
        }
    }
}

@Composable
private fun ShowTraining(
    modifier: Modifier = Modifier,
    profile: Profile,
    training: Training,
    source: Source
) {
    val dateConverter: DateConverter by inject()
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
        ) {
            Row(modifier = Modifier.padding(dimensionResource(R.dimen.big_margin))) {
                AsyncImage(
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.training_item_icon_size))
                        .clip(CircleShape)
                        .align(Alignment.CenterVertically),
                    model = profile.imageUrlMedium,
                    placeholder = painterResource(id = R.drawable.no_photo),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.big_margin)))
                Column() {
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.small_margin)))
                    Text(
                        text = "${profile.firstName} ${profile.lastName}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.small_margin)))
                    Row {
                        Image(
                            painter = painterResource(id = getSportByName(training.sport).icon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(size = dimensionResource(R.dimen.training_detail_sport_icon_size))
                                .align(Alignment.CenterVertically)
                        )
                        Text(
                            text = dateConverter.getDateTimeString(training.startDate),
                            style = MaterialTheme.typography.caption,
                            modifier = Modifier
                                .padding(start = dimensionResource(R.dimen.small_margin))
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.normal_margin)))
            Column(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.big_margin))) {
                Text(
                    text = training.name,
                    style = MaterialTheme.typography.h6
                )
                if (training.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.normal_margin)))
                    Text(
                        text = training.description,
                        style = MaterialTheme.customTypography.bodyWeak
                    )
                }
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.big_margin)))
                TimeDistanceSpeed(training = TrainingConverter.fromTrainingToTrainingListItem(training))
            }
            ShowPhotos(training = training)
        }
    }
}

@Composable
private fun ShowPhotos(
    modifier: Modifier = Modifier,
    training: Training
) {
    if (!training.photoUrls.isEmpty()) {
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.big_margin)))
        LazyRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.normal_margin)),
            contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.big_margin))
        ) {
            training.photoUrls.forEach { url ->
                item {
                    AsyncImage(
                        modifier = Modifier
                            .size(
                                dimensionResource(R.dimen.training_detail_photo_size_X),
                                dimensionResource(R.dimen.training_detail_photo_size_Y)
                            )
                            .clip(MaterialTheme.shapes.medium),
                        model = url,
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.no_photo),
                        contentDescription = null
                    )
                }
            }
        }
    }
}