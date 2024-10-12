package org.slade.chase.ui.progress

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.MutableStateFlow
import org.slade.chase.models.BytesReadCarrier
import org.slade.chase.models.DownloadItem

@Composable
fun DownloadItem.DownloadProgress(
    modifier: Modifier,
    bytesReadPartFlows: List<MutableStateFlow<BytesReadCarrier>>
) {
    LinearByteProgress(
        modifier = Modifier
            .then(modifier),
        bytesReadFlows = bytesReadPartFlows
    )
}