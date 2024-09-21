package org.slade.chase.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableStateFlow
import org.slade.chase.models.BytesReadCarrier
import org.slade.chase.models.DownloadItem
import org.slade.chase.models.DownloadPart
import org.slade.chase.suffixByteSize

@Composable
fun DownloadItem.DownloadProgress(
    modifier: Modifier = Modifier,
    bytesReadPartFlows: List<MutableStateFlow<BytesReadCarrier>>
) {

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        parts.mapIndexed { index, downloadPart ->
            downloadPart.ByteProgress(
                modifier = Modifier
                    .weight(1f),
                bytesReadFlow = bytesReadPartFlows[index],
                isLast = index == parts.size - 1,
                isFirst = index == 0
            )
        }
    }
}

@Composable
fun DownloadPart.ByteProgress(
    modifier: Modifier = Modifier,
    bytesReadFlow: MutableStateFlow<BytesReadCarrier>,
    isFirst: Boolean,
    isLast: Boolean
) {

    val rangeLength by remember {
        mutableStateOf((end - offset).let { if(it >= 0) it + 1 else 0 })
    }

    val background = MaterialTheme.colors.primary

    val bytesReadCarrier by bytesReadFlow.collectAsState()

    val progress by animateFloatAsState(
        targetValue = bytesReadCarrier.numberOfBytes.toFloat(),
        animationSpec = tween(300)
    )

    Box(
        modifier = Modifier
            .height(40.dp)
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            drawRect(
                color = Color(190, 24, 93, 100) ,
                topLeft = Offset(0f, 0f),
                size = size
            )

            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(190, 24, 93),
                        Color(157, 23, 77),
                        Color(190, 24, 93),
                    )
                ),
                topLeft = Offset(0f, 0f),
                size = Size(
                    width = (progress / rangeLength) * size.width,
                    height = size.height
                )
            )
        }

        Text(
            text = progress.toDouble().suffixByteSize(),
            fontSize = 12.sp,
            color = Color.White
        )
    }
}