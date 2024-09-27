package org.slade.chase.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.slade.chase.models.BytesReadCarrier
import org.slade.chase.models.DownloadItem
import org.slade.chase.tasks.deserializeDownloadItems
import org.slade.chase.ui.DownloadListItem

@Composable
fun Downloads() {
    val coroutineScope = rememberCoroutineScope()

    var downloadItems by remember {
        mutableStateOf<List<Pair<DownloadItem, List<MutableStateFlow<BytesReadCarrier>>>>>(emptyList())
    }

    LaunchedEffect("DeserializeDownloadItems") {
        coroutineScope.launch {
            downloadItems = deserializeDownloadItems().map { downloadItem ->
                downloadItem to downloadItem.parts.map { MutableStateFlow(BytesReadCarrier(it.id, it.index, it.retrieved)) }
            }
        }
    }

    LazyColumn(
        modifier = Modifier
    ) {
        items(downloadItems) { (downloadItem, bytesReadStateFlows) ->
            DownloadListItem(
                downloadItem,
                bytesReadPartFlows = bytesReadStateFlows
            )
        }
    }
}