package org.slade.chase.ui.progress

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
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
import org.slade.chase.ui.ChaseWindow

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
            512.dp,
            424.dp
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
        ChaseWindow(
            onCloseRequest = { showProgressWindow = false },
            state = progressWindowState,
            resizable = false,
            icon = painterResource(Res.drawable.compose_multiplatform)
        ) {
            Column (
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
//                ByteProgress(
//                    bytesReadFlows = bytesReadPartFlows,
//                    strokeWidth = 10f,
//                    radius = 40f
//                )
                SpeedoMeter(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(10.dp),
                    config = SpeedoMeterConfig().apply {
                        needleBaseGap = 4f
                        progressStrokeWidth = 48f
                        trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.125f)
                        needleBaseRadius = 24f
                        needleSectorAngle = 70f
                        sweepAngle = 180f
                    },
                    value = 75f
                )
            }
        }
    }
}