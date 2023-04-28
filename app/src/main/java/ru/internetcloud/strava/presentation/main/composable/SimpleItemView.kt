package ru.internetcloud.strava.presentation.main.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.internetcloud.strava.R
import ru.internetcloud.strava.presentation.main.model.Item
import ru.internetcloud.strava.presentation.main.model.SimpleItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.SimpleItemView(item: SimpleItem, onLikeClickListener: (Item) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .animateItemPlacement(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = item.title,
            fontWeight = FontWeight.Bold
        )
        Text(text = item.author)
        Text(text = "id = ${item.id}")
        Row {
            Icon(
                modifier = Modifier.clickable {
                    onLikeClickListener(item)
                },
                painter = painterResource(id = R.drawable.ic_like),
                contentDescription = null,
                tint = colorResource(id = R.color.tangelo_dark)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = String.format(
                    stringResource(R.string.main_likes_amount),
                    item.likesCount.toString()
                ),
                color = colorResource(id = R.color.tangelo)
            )
        }
    }
}
