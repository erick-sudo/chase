package org.slade.chase.ui.progress

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import chase.composeapp.generated.resources.Res
import chase.composeapp.generated.resources.compose_multiplatform
import kotlinx.coroutines.flow.MutableStateFlow
import org.jetbrains.compose.resources.painterResource
import org.slade.chase.models.BytesReadCarrier
import org.slade.chase.models.DownloadItem

@Composable
actual fun DownloadItem.DownloadProgress(
    modifier: Modifier,
    bytesReadPartFlows: List<MutableStateFlow<BytesReadCarrier>>
) {
    var showProgressWindow by remember {
        mutableStateOf(true)
    }

    val progressWindowState = rememberWindowState(
        size = DpSize(
            400.dp,
            400.dp
        )
    )

    Column(
        modifier = Modifier
            .then(modifier)
    ) {
        Text(text = "Downloading")

        Crossfade(showProgressWindow) { show ->

            Button(
                onClick = {
                    showProgressWindow = !show
                }
            ) {
                Text(text = if(show) "Hide" else "Progress")
            }
        }
    }

    if(showProgressWindow) {
        Window(
            onCloseRequest = { showProgressWindow = false },
            state = progressWindowState,
            undecorated = false,
            resizable = false,
            title = "Download Progress",
            icon = painterResource(Res.drawable.compose_multiplatform)
        ) {
            Column (
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                ByteProgress(
                    bytesReadFlows = bytesReadPartFlows,
                    strokeWidth = 10f,
                    radius = 40f
                )
            }
        }
    }
}