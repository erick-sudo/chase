package org.slade.chase.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.ScreenKey
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.slade.chase.models.BytesReadCarrier
import org.slade.chase.models.DownloadItem
import org.slade.chase.models.DownloadPart
import org.slade.chase.suffixByteSize
import org.slade.chase.tasks.deserializeDownloadItem
import org.slade.chase.ui.progress.CircularByteProgress
import org.slade.chase.ui.theme.ChaseTheme
import java.nio.file.Path

@Composable
actual fun DownloadItemDetails(
    modifier: Modifier,
    downloadId: String
) {

    var exploded by remember {
        mutableStateOf(false)
    }

    var downloadItem by remember {
        mutableStateOf<DownloadItem?>(null)
    }

    var bytesReadFlows by remember {
        mutableStateOf<List<MutableStateFlow<BytesReadCarrier>>>(emptyList())
    }

    val bytesReadCombinedFlow = remember(downloadItem) {
        combine(bytesReadFlows.map { it.asStateFlow() }) { bytesReadCarriers ->
            bytesReadCarriers.map { it.numberOfBytes }
        }
    }

    val retrievedBytes by bytesReadCombinedFlow.collectAsState(emptyList())

    LaunchedEffect("FetchDownloadItem") {
        downloadItem = deserializeDownloadItem(downloadId).also { item ->
            bytesReadFlows = item.parts.map { MutableStateFlow(BytesReadCarrier(it.id, it.index, it.retrieved)) }
        }
    }

    LaunchedEffect(downloadItem) {
        downloadItem?.let { item ->
            bytesReadFlows.mapIndexed { index, flw ->
                launch {
                    val part = item.parts.getOrNull(index)
                    part?.let { p ->
                        val size = (p.end - p.offset) + 1
                        var i = p.retrieved
                        while (true) {
                            val retrieved = if(i>size) size else i
                            p.retrieved = retrieved
                            flw.value = flw.value.copy(numberOfBytes = retrieved)
                            delay(100)
                            if(i > size) {
                                break
                            }
                            i += (5120..10240).random()
                            // i += (1..10).random()
                        }
                    }
                }
            }.joinAll()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        OutlinedCard (
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedVisibility(
                    visible = downloadItem == null,
                ) {
                    Column(
                        modifier = Modifier
                            .height(100.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Loading...",
                            modifier = Modifier
                                .padding(vertical = 8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            maxLines = 1
                        )
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            strokeCap = StrokeCap.Round
                        )
                    }
                }

                AnimatedVisibility(
                    visible = downloadItem != null,
                    modifier = Modifier
                        .fillMaxWidth(),
                    enter = slideInHorizontally(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioHighBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        downloadItem?.let { item ->
                            item.CircularByteProgress(
                                modifier = Modifier
                                    .size(150.dp),
                                bytesReadFlows = bytesReadFlows,
                                radius = 50f
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            val retrieved = retrievedBytes.sum()
                            val completion = (retrieved.toFloat()/item.contentLength.toFloat())
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .wrapContentHeight()
                            ) {
                                Text(
                                    text = "File Size: ${item.contentLength.toDouble().suffixByteSize()}",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Retrieved: ${retrieved.toDouble().suffixByteSize()}",
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(horizontal = 8.dp, vertical = 4.dp),
                                        maxLines = 1
                                    )

                                    Text(
                                        text = "${(completion * 100).toInt()}%",
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp, vertical = 4.dp),
                                        maxLines = 1,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                LinearProgressIndicator(
                                    progress = { completion },
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                        .fillMaxWidth(),
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    strokeCap = StrokeCap.Round
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {

                                    Text(
                                        text = "${ Path.of(item.source).fileName}",
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                            .weight(1f),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    OutlinedCard(

                                    ) {
                                        Row (
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            (1..4).mapIndexed { index, _ ->
                                                if(index > 0) {
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                }
                                                TextButton(
                                                    onClick = {

                                                    },
                                                    contentPadding = PaddingValues(2.dp),
                                                    shape = RoundedCornerShape(10.dp),
                                                    modifier = Modifier.size(32.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Outlined.Face,
                                                        contentDescription = ""
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))
                                    ElevatedButton(
                                        onClick = {
                                            exploded = !exploded
                                        },
                                        colors = ButtonDefaults.elevatedButtonColors(
                                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                        ),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text(
                                            text = if(exploded) "Collapse" else "Divide and Conquer",
                                            style = MaterialTheme.typography.bodySmall,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            visible = exploded,
            enter = slideInVertically (
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioHighBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(360.dp),
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                val partFlowPairs:  List<Pair<DownloadPart, MutableStateFlow<BytesReadCarrier>>> = downloadItem?.parts?.zip(bytesReadFlows) ?: emptyList()
                items(items = partFlowPairs) { (part, flw) ->
                    DownloadPartDetails(
                        modifier = Modifier
                            .padding(8.dp),
                        part = part,
                        flw = flw
                    )
                }
            }
        }
    }
}

@Composable
private fun DownloadPartDetails(
    modifier: Modifier = Modifier,
    part: DownloadPart,
    flw: MutableStateFlow<BytesReadCarrier>
) {

    val retrieved by flw.collectAsState()

    val size = remember {
        (part.end - part.offset) + 1
    }

    OutlinedCard(
        modifier = Modifier
            .then(modifier)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            listOf(
                "Offset Byte:" to part.offset,
                "Last Byte:" to part.end,
                "Total:" to size,
            ).map { pair ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    PairDisplay(
                        pair = pair.let { it.first to it.second.toString() },
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Retrieved",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    textAlign = TextAlign.Right
                )

                Spacer(
                    modifier = Modifier
                        .weight(1f)
                )

                Text(
                    text = "${retrieved.numberOfBytes} of $size bytes",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            LinearProgressIndicator(
                progress = { retrieved.numberOfBytes.toFloat()/size.toFloat() },
                modifier = Modifier
                    .padding(0.dp)
                    .fillMaxWidth(),
                color = MaterialTheme.colorScheme.primaryContainer,
                strokeCap = StrokeCap.Round
            )

            Text(
                text = "${((retrieved.numberOfBytes/size.toFloat()) * 100f)}%",
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Right,
                color = MaterialTheme.colorScheme.primaryContainer,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row (
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                (1..4).mapIndexed { index, _ ->
                    if(index > 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    TextButton(
                        onClick = {

                        },
                        contentPadding = PaddingValues(2.dp),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Face,
                            contentDescription = ""
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PairDisplay(
    modifier: Modifier = Modifier,
    pair: Pair<String, String>
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = pair.first,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp),
            textAlign = TextAlign.Right
        )

        Spacer(
            modifier = Modifier
                .height(1.dp)
                .border(
                    width = 0.3.dp,
                    brush = Brush.linearGradient(colors = ChaseTheme.borderGradientColors.map { it.copy(alpha = 0.3f) }),
                    shape = RectangleShape
                )
                .weight(1f)
        )

        Text(
            text = pair.second,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}