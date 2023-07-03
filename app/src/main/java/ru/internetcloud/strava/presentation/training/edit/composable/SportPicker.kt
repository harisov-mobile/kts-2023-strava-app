package ru.internetcloud.strava.presentation.training.edit.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.internetcloud.strava.R
import ru.internetcloud.strava.domain.common.model.Sport
import ru.internetcloud.strava.domain.common.model.SportType
import ru.internetcloud.strava.domain.common.model.getSportsWithSportType

@Composable
fun SportPicker(
    currentSport: Sport,
    onSportSelect: (Sport) -> Unit
) {
    val sportsWithSportType: List<Any> = rememberSaveable { getSportsWithSportType() }
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val currentItemIndex = getCurrentSportIndex(sportsWithSportType, currentSport)

    Column(modifier = Modifier.fillMaxWidth()) {
        Divider(
            color = MaterialTheme.colors.onSurface.copy(alpha = .2f),
            modifier = Modifier
                .padding(top = 8.dp)
                .width(30.dp)
                .height(3.dp)
                .align(
                    alignment = Alignment.CenterHorizontally
                )
        )
        Text(
            text = "Choose a Sport",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(8.dp)
                .align(
                    alignment = Alignment.CenterHorizontally
                )

        )
        Divider(
            color = MaterialTheme.colors.onSurface.copy(alpha = .2f),
        )

        LazyColumn(modifier = Modifier.fillMaxWidth(), state = listState) {
            items(
                items = sportsWithSportType,
                contentType = { it::class.java.name }
            )
            { item ->
                when (item) {
                    is SportType -> SportTypeItem(sportType = item)
                    is Sport -> SportItem(sport = item, currentSport = currentSport, onSportSelect = onSportSelect)
                }
            }
        }

        LaunchedEffect(key1 = currentItemIndex) {
            coroutineScope.launch {
                listState.scrollToItem(index = currentItemIndex)
            }
        }
    }
}

@Composable
fun SportItem(
    modifier: Modifier = Modifier,
    sport: Sport,
    currentSport: Sport,
    onSportSelect: (Sport) -> Unit
) {
    val textColor: Color
    if (sport == currentSport) {
        textColor = MaterialTheme.colors.primary
    } else {
        textColor = MaterialTheme.colors.onSurface
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.sport_item_height))
                .clickable(
                    onClick = remember {
                        {
                            onSportSelect(sport)
                        }
                    }
                )
        ) {
            Image(
                painter = painterResource(id = sport.icon),
                colorFilter = ColorFilter.tint(textColor),
                contentDescription = null,
                modifier = Modifier
                    .padding(horizontal = dimensionResource(R.dimen.big_margin))
                    .size(size = dimensionResource(R.dimen.sport_item_icon_size))
                    .align(Alignment.CenterVertically)
            )

            Text(
                text = stringResource(id = sport.label),
                color = textColor,
                fontSize = 16.sp,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )
        }
        Divider(
            color = MaterialTheme.colors.onSurface.copy(alpha = .2f),
        )
    }
}

@Composable
fun SportTypeItem(
    modifier: Modifier = Modifier,
    sportType: SportType
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.sport_item_height))
            .background(color = Color.LightGray)
    ) {
        Text(
            text = stringResource(id = sportType.label),
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

fun getCurrentSportIndex(list: List<Any>, currentSport: Sport): Int {
    var currentIndex = 0
    list.forEachIndexed { index, item ->
        if (item is Sport && item == currentSport) {
            currentIndex = index
        }
    }
    return currentIndex
}

@Preview
@Composable
fun SportPickerPreview() {

    SportPicker(
        currentSport = Sport.Badminton,
        onSportSelect = { }
    )
}
