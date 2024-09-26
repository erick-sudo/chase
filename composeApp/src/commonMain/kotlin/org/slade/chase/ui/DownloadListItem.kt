package org.slade.chase.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.slade.chase.models.BytesReadCarrier
import org.slade.chase.models.DownloadItem
import org.slade.chase.models.DownloadState
import org.slade.chase.tasks.serialize
import org.slade.chase.ui.progress.DownloadProgress

@Composable
fun DownloadListItem(
    downloadItem: DownloadItem,
    bytesReadPartFlows: List<MutableStateFlow<BytesReadCarrier>>
) {

    var showContextMenu by remember {
        mutableStateOf(false)
    }

    val instructionChannel by remember {
        mutableStateOf(Channel<suspend () -> Unit>())
    }

    val coroutineScope = rememberCoroutineScope()

    var currentJob by remember {
        mutableStateOf<Pair<DownloadState, Job?>>(DownloadState.Initializing to null)
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
    }

    Column(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
    ) {

        Spacer(modifier = Modifier.height(5.dp))

        Text(text = downloadItem.parts.joinToString(separator = " - ") { "${it.retrieved}" })

        Spacer(modifier = Modifier.height(5.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Crossfade(
                targetState = currentJob,
                modifier = Modifier
                    .weight(1f)
            ) { (state, _) ->
                when(state) {
                    DownloadState.Resumed -> {
                        downloadItem.DownloadProgress(
                            modifier = Modifier
                                .fillMaxWidth(),
                            bytesReadPartFlows = bytesReadPartFlows
                        )
                    }
                    else -> Text(
                        modifier = Modifier
                            .weight(1f),
                        text = state.name
                    )
                }
            }

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
                        onDismissRequest = { showContextMenu = false },
                        modifier = Modifier
//                            .shadow(
//                                elevation = 0.dp,
//                                ambientColor = Color.White,
//                                spotColor = Color.White
//                            )
//                            .border(
//                                width = 1.dp,
//                                shape = RoundedCornerShape(8.dp),
//                                brush = Brush.sweepGradient(
//                                    colors = listOf(
//                                        MaterialTheme.colorScheme.primary,
//                                        MaterialTheme.colorScheme.inversePrimary,
//                                        MaterialTheme.colorScheme.primary
//                                    )
//                                ))
//                        .background(color = MaterialTheme.colorScheme.background),
                    ) {
                        // listOf("Resume", "Preview", "Restart", "Stop", "Cancel", "Delete")
                        DropdownMenuItem(
                            text = {
                                Text(text = "Resume")
                            },
                            leadingIcon = {
                                Icon(imageVector = Icons.Outlined.ThumbUp, contentDescription = "Resume")
                            },
                            onClick = {
                                currentJob = DownloadState.Starting to null
                                showContextMenu = false
                            },
                        )

                        DropdownMenuItem(
                            text = {
                                Text(text = "Pause")
                            },
                            onClick = {
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
                            }
                        )
                    }
                }
            }
        }
    }
}