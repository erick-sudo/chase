package org.slade.chase.ui.progress

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.MutableStateFlow
import org.slade.chase.models.BytesReadCarrier
import org.slade.chase.models.DownloadItem

@Composable
expect fun DownloadItem.DownloadProgress(
    modifier: Modifier = Modifier,
    bytesReadPartFlows: List<MutableStateFlow<BytesReadCarrier>>
)