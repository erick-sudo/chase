package org.slade.chase.ui.screens.downloads

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.slade.chase.models.BytesReadCarrier
import org.slade.chase.models.DownloadItem
import org.slade.chase.ui.DownloadListItem
import org.slade.chase.ui.screens.DownloadItemDetailsScreen
import org.slade.chase.ui.theme.ChaseTheme

class ActiveDownloadsScreen: Screen {
    @Composable
    override fun Content() {
        ActiveDownloads()
    }
}

@Composable
private fun ActiveDownloads() {
    val navigator = LocalNavigator.current
    val coroutineScope = rememberCoroutineScope()

    var downloadItems by remember {
        mutableStateOf<List<Pair<DownloadItem, List<MutableStateFlow<BytesReadCarrier>>>>>(emptyList())
    }

    val downloadsViewModel = ChaseLocalDownloadsViewModel.current

    LaunchedEffect("DeserializeDownloadItems") {
        coroutineScope.launch {
//            downloadItems = deserializeDownloadItems().map { downloadItem ->
//                downloadItem to downloadItem.parts.map { MutableStateFlow(BytesReadCarrier(it.id, it.index, it.retrieved)) }
//            }
//            val items = mutableListOf<Pair<DownloadItem, List<MutableStateFlow<BytesReadCarrier>>>>()
//            FakeRepo.downloadItems.map { url ->
//                launch {
//                    DownloadItem.init(url).also { downloadItem ->
//                        items += (downloadItem to downloadItem.parts.map { MutableStateFlow(BytesReadCarrier(it.id, it.index, it.retrieved)) })
//                    }
//                }
//            }.joinAll()
//            downloadItems = items
        }
    }

    LazyColumn(
        modifier = Modifier
    ) {
        item {
            Button(
                onClick = {
                    coroutineScope.launch {
                        downloadsViewModel?.serialize(downloadItems.map { it.first })
                    }
                }
            ) {
                Text("Serialize")
            }
        }

        itemsIndexed(downloadItems) { index, (downloadItem, bytesReadStateFlows) ->
            DownloadListItem(
                modifier = Modifier
                    .clickable {
                        navigator?.push(
                            DownloadItemDetailsScreen(
                                downloadId = downloadItem.id
                            )
                        )
                    }
                    .padding(8.dp),
                downloadItem = downloadItem,
                bytesReadPartFlows = bytesReadStateFlows
            )

            if(index < downloadItems.size - 1) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .border(
                            width = 1.dp,
                            brush = Brush.linearGradient(
                                colors = ChaseTheme.borderGradientColors.map { it.copy(alpha = 0.4f) }
                            ),
                            shape = RectangleShape
                        )
                )
            }
        }
    }
}