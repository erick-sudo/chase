package org.slade.chase.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun DownloadItemDetails(
    modifier: Modifier = Modifier,
    downloadId: String
)