package ru.internetcloud.strava.presentation.oldmain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.internetcloud.strava.presentation.oldmain.composable.ComplexItemView
import ru.internetcloud.strava.presentation.oldmain.composable.SimpleItemView
import ru.internetcloud.strava.presentation.oldmain.model.ComplexItem
import ru.internetcloud.strava.presentation.oldmain.model.Item
import ru.internetcloud.strava.presentation.oldmain.model.SimpleItem

class OldMainFragment : Fragment() {

    private val viewModel by viewModels<OldMainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            val listState by viewModel.listState.collectAsStateWithLifecycle(
                lifecycleOwner = viewLifecycleOwner
            )
            LazyColumn {
                itemsWithListState<Item>(
                    items = listState.itemsWithState(),
                    key = { item -> item.id },
                    contentType = { it::class.java.name }
                ) { index, item ->
                    when (item) {
                        is SimpleItem -> SimpleItemView(
                            item = item,
                            onLikeClickListener = { item ->
                                viewModel.increaseLikes(item)
                            }
                        )
                        is ComplexItem -> ComplexItemView(item = item, onItemClick = {})
                    }
                    listState.loadNextPage(index = index, onLoadNextPage = viewModel::loadNextPage)
                    Divider(color = Color.Gray)
                }
            }
        }
    }
}
