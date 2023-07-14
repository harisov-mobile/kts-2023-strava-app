package ru.internetcloud.strava.presentation.web

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import ru.internetcloud.strava.R
import ru.internetcloud.strava.presentation.MainActivity
import ru.internetcloud.strava.presentation.common.compose.TopAppBarWithNavigationIconAndLogout

@Composable
fun WebScreen(link: String) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBarWithNavigationIconAndLogout(
                title = stringResource(id = R.string.web_title),
                onArrowBackPressed = {
                    (context as MainActivity).onBackPressed()
                }
            )
        }
    ) { paddingContent ->
        Box(modifier = Modifier.padding(paddingContent)) {
            // Adding a WebView inside AndroidView
            // with layout as full screen
            AndroidView(factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    webViewClient = WebViewClient()
                    loadUrl(link)
                }
            }, update = {
                it.loadUrl(link)
            })
        }
    }
}
