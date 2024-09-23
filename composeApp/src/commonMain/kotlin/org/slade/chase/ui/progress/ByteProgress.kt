package org.slade.chase.ui.progress

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import org.slade.chase.models.BytesReadCarrier
import org.slade.chase.models.DownloadItem
import org.slade.chase.ui.drawDoughnut

@Composable
fun DownloadItem.ByteProgress(
    modifier: Modifier = Modifier
        .size(100.dp),
    strokeWidth: Float = 10f,
    radius: Float,
    bytesReadFlows: List<MutableStateFlow<BytesReadCarrier>>,
) {

    val gradientColors by remember {
        mutableStateOf(listOf(
            Color.Red,
            Color.Yellow,
            Color.Magenta,
            Color.Red
        ))
    }

    val combinedFlow = remember {
        combine(bytesReadFlows.map { it.asStateFlow() }) { byteCarriers ->
            byteCarriers.zip(parts).map { (byteCarrier, part) ->

                val window = (part.end - part.offset).let { if(it > 0) it + 1 else 1 }.toFloat()

                val windowAngle = (window / contentLength) * 360

                val offsetAngle = (part.offset.toFloat() / contentLength) * 360

                val sweepAngle = (byteCarrier.numberOfBytes / window) * windowAngle

                ProgressSegment(
                    offsetAngle = offsetAngle,
                    sweepAngle = sweepAngle,
                    windowAngle = windowAngle
                )
            }
        }
    }

    val segments by combinedFlow.collectAsState(emptyList())

    Canvas(
        modifier = Modifier
            .then(modifier)
    ) {
        drawDoughnut(
            brush = Brush.sweepGradient(colors = gradientColors.map { it.copy(alpha = 0.3f) }),
            startAngle = 0f,
            sweepAngle = 360f,
            radius = radius,
            strokeWidth = strokeWidth,
            center = Offset(
                x = size.width/2,
                y = size.height/2
            )
        )

        segments.forEach { segment ->
            drawDoughnut(
                brush = Brush.sweepGradient(colors = gradientColors),
                startAngle = segment.offsetAngle,
                sweepAngle = segment.sweepAngle,
                radius = radius,
                strokeWidth = strokeWidth,
                center = Offset(
                    x = size.width/2,
                    y = size.height/2
                )
            )
        }
    }
}