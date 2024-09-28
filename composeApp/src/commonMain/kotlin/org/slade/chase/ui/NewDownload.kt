package org.slade.chase.ui

import androidx.compose.runtime.Composable
import org.slade.chase.models.DownloadItem

@Composable
expect fun NewDownload(
    onSuccess: (DownloadItem) -> Unit,
    onCancel: () -> Unit
)