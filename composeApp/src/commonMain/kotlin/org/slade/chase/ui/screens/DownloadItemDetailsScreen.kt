package org.slade.chase.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey


class DownloadItemDetailsScreen(
    val downloadId: String
): Screen {

    override val key: ScreenKey
        get() = "DownloadItemDetails"

    @Composable
    override fun Content() {
        DownloadItemDetails(
            downloadId = downloadId
        )
    }
}

@Composable
expect fun DownloadItemDetails(
    modifier: Modifier = Modifier,
    downloadId: String
)