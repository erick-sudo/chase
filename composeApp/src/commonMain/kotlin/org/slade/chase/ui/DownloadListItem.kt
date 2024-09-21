package org.slade.chase.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.slade.chase.models.BytesReadCarrier
import org.slade.chase.models.DownloadItem
import org.slade.chase.models.DownloadState

@Composable
fun DownloadListItem(
    downloadItem: DownloadItem,
    bytesReadPartFlows: List<MutableStateFlow<BytesReadCarrier>>
) {

    var showContextMenu by remember {
        mutableStateOf(false)
    }

    val coroutineScope = rememberCoroutineScope()

    val downloadState by remember {
        mutableStateOf(DownloadState.Initializing)
    }

    LaunchedEffect("State") {
        coroutineScope.launch {
            bytesReadPartFlows.mapIndexed { index, flw ->
                launch {
                    val part = downloadItem.parts.getOrNull(index)
                    part?.let { p ->
                        val size = (p.end - p.offset) + 1
                        var i = 0L
                        while (true) {
                            flw.value = flw.value.copy(numberOfBytes =  if(i>size) size else i)
                            delay(1)
                            if(i > size) {
                                break
                            }
                            i += (512..1024).random()
                        }
                    }
                }
            }.joinAll()
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        downloadItem.DownloadProgress(
            modifier = Modifier
                .weight(1f),
            bytesReadPartFlows = bytesReadPartFlows
        )

        Spacer(modifier = Modifier.width(5.dp))

        IconButton(
            modifier = Modifier,
            onClick = { showContextMenu = true }
        ) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "More",
                modifier = Modifier
                    .size(16.dp),
                tint = Color(190, 24, 93)
            )
        }

        AnimatedVisibility(showContextMenu) {
            Dialog(
                onDismissRequest = { showContextMenu = false },
            ) {
                Column {
                    listOf("Resume", "Preview", "Restart", "Stop", "Cancel", "Delete").forEach {
                        Button(
                            onClick = {},
                            modifier = Modifier
                        ) {
                            Text(text = it)
                        }
                    }
                }
            }
        }
    }
}

//@Preview
//@Composable
//fun DownloadListItemPreview() {
//    MaterialTheme {
//        //DownloadListItem()
//    }
//}