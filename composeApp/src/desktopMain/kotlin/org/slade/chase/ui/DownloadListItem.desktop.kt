package org.slade.chase.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import chase.composeapp.generated.resources.Res
import chase.composeapp.generated.resources.outline_pause_circle_24
import chase.composeapp.generated.resources.outline_play_circle_24
import chase.composeapp.generated.resources.outline_stop_circle_24
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.vectorResource
import org.slade.chase.models.BytesReadCarrier
import org.slade.chase.models.DownloadItem
import org.slade.chase.models.DownloadState
import org.slade.chase.suffixByteSize
import org.slade.chase.tasks.serialize
import org.slade.chase.ui.progress.LinearByteProgress
import org.slade.chase.ui.progress.SpeedoMeter
import org.slade.chase.ui.progress.SpeedoMeterConfig
import java.nio.file.Path

@Composable
actual fun DownloadListItem(
    modifier: Modifier,
    downloadItem: DownloadItem,
    bytesReadPartFlows: List<MutableStateFlow<BytesReadCarrier>>
) {
    var showContextMenu by remember {
        mutableStateOf(false)
    }

    val instructionChannel by remember {
        mutableStateOf(Channel<suspend () -> Unit>())
    }

    val retrievedBytesFlow = remember {
        combine(bytesReadPartFlows.map { it.asStateFlow() }) { bytesReadCarriers ->
            bytesReadCarriers.map { byteCarrier ->
                byteCarrier.numberOfBytes
            }
        }
    }

    val retrievedBytes by retrievedBytesFlow.collectAsState(emptyList())

    val coroutineScope = rememberCoroutineScope()

    var currentJob by remember {
        mutableStateOf<Pair<DownloadState, Job?>>(DownloadState.Initializing to null)
    }

    var speed by remember {
        mutableStateOf(0f)
    }

    LaunchedEffect(Unit) {
        for(instruction in instructionChannel) {
            coroutineScope.launch {
                instruction()
            }
        }
    }

    LaunchedEffect(currentJob.first) {
        if(currentJob.first == DownloadState.Starting) {
            currentJob = DownloadState.Resumed to coroutineScope.launch {

                bytesReadPartFlows.mapIndexed { index, flw ->
                    launch {
                        val part = downloadItem.parts.getOrNull(index)
                        part?.let { p ->
                            val size = (p.end - p.offset) + 1
                            var i = p.retrieved
                            while (true) {
                                val retrieved = if(i>size) size else i
                                p.retrieved = retrieved
                                flw.value = flw.value.copy(numberOfBytes = retrieved)
                                speed = (35..100).random().toFloat()
                                delay(100)
                                if(i > size) {
                                    break
                                }
                                i += (512..1024).random()
                                // i += (1..10).random()
                            }
                        }
                    }
                }.joinAll()
            }
        }
    }

    Row(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(48.dp)
        ) {
            SpeedoMeter(
                modifier = Modifier
                    .fillMaxSize(),
                config = SpeedoMeterConfig().apply {
                    needleBaseGap = 0f
                    progressStrokeWidth = 4f
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.125f)
                    needleBaseRadius = 4f
                    needleSectorAngle = 90f
                    sweepAngle = 240f
                },
                value = speed
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .wrapContentHeight(),
        ) {
            Text(
                text = "${Path.of(downloadItem.source).fileName}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

//            downloadItem.DownloadProgress(
//                modifier = Modifier
//                    .height(5.dp)
//                    .fillMaxWidth(),
//                bytesReadPartFlows = bytesReadPartFlows
//            )
            downloadItem.LinearByteProgress(
                modifier = Modifier
                    .height(10.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
//                    .border(
//                        width = 1.dp,
//                        brush = Brush.linearGradient(ChaseTheme.borderGradientColors.map { it.copy(alpha = 0.3f) }),
//                        shape = RoundedCornerShape(8.dp)
//                    ),
                        ,
                bytesReadFlows = bytesReadPartFlows
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    text = "Done in 23 minutes",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 10.sp,
                    maxLines = 1
                )

                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(
                            color = MaterialTheme.colorScheme.primary
                        )) {
                            append("${retrievedBytes.sum().toDouble().suffixByteSize()} / ")
                        }

                        withStyle(SpanStyle(
                            color = Color(190, 24, 93)
                        )) {
                            append(downloadItem.contentLength.toDouble().suffixByteSize())
                        }
                    },
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }
        }

//        Crossfade(
//            targetState = currentJob,
//            modifier = Modifier
//                .weight(1f)
//        ) { (state, _) ->
//            when(state) {
//                DownloadState.Resumed -> {
//                    downloadItem.DownloadProgress(
//                        modifier = Modifier
//                            .fillMaxWidth(),
//                        bytesReadPartFlows = bytesReadPartFlows
//                    )
//                }
//                else -> Text(
//                    modifier = Modifier
//                        .weight(1f),
//                    text = state.name
//                )
//            }
//        }

        TextButton(
            modifier = Modifier
                .size(36.dp),
            contentPadding = PaddingValues(2.dp),
            onClick = { showContextMenu = true }
        ) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "More",
                tint = Color(190, 24, 93)
            )
        }

        AnimatedVisibility(
            visible = showContextMenu,
            enter = expandIn(
                animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy, stiffness = Spring.StiffnessLow)
            )
        ) {
            MaterialTheme(
                shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(8.dp))
            ) {
                DropdownMenu(
                    expanded = showContextMenu,
                    onDismissRequest = { showContextMenu = false }
                ) {
                    listOf(
                        "Pause" to Res.drawable.outline_pause_circle_24 to {

                        },
                        "Resume" to Res.drawable.outline_play_circle_24 to {
                            currentJob = DownloadState.Starting to null
                            showContextMenu = false
                        },
                        "Stop" to Res.drawable.outline_stop_circle_24 to {
                            coroutineScope.launch {
                                instructionChannel.send {
                                    currentJob.second?.let {
                                        if(it.isActive) {
                                            it.cancel("Cancel Download")
                                        }
                                    }
                                    downloadItem.serialize()
                                    showContextMenu = false
                                }
                            }
                            Unit
                        }
                    ).forEach { (drawable, action) ->
                        val (desc, icon) = drawable
                        DropdownMenuItem(
                            text = {
                                Text(text = desc)
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = vectorResource(icon),
                                    contentDescription = desc
                                )
                            },
                            onClick = action,
                        )
                    }
                }
            }
        }
    }
}