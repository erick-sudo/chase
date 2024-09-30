package org.slade.chase.ui.progress

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.flow.MutableStateFlow
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
            512.dp,
            424.dp
        )
    )

//    CircularByteProgress(
//        modifier = Modifier
//            .then(modifier),
//        bytesReadFlows = bytesReadPartFlows,
//        strokeWidth = 10f,
//        radius = 40f
//    )

    LinearByteProgress(
        modifier = Modifier
            .then(modifier),
        bytesReadFlows = bytesReadPartFlows
    )

//    if(showProgressWindow) {
//        ChaseWindow(
//            onCloseRequest = { showProgressWindow = false },
//            state = progressWindowState,
//            resizable = false,
//            icon = painterResource(Res.drawable.compose_multiplatform)
//        ) {
//            Column (
//                modifier = Modifier
//                    .fillMaxWidth(),
//            ) {
//                SpeedoMeter(
//                    modifier = Modifier
//                        .weight(1f)
//                        .fillMaxWidth()
//                        .padding(10.dp),
//                    config = SpeedoMeterConfig().apply {
//                        needleBaseGap = 4f
//                        progressStrokeWidth = 48f
//                        trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.125f)
//                        needleBaseRadius = 24f
//                        needleSectorAngle = 70f
//                        sweepAngle = 180f
//                    },
//                    value = 75f
//                )
//            }
//        }
//    }
}