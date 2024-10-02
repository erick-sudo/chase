package org.slade.chase.ui.monitors

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.slade.chase.some
import org.slade.chase.ui.closestMultiple
import org.slade.chase.ui.getXY
import org.slade.chase.ui.lineTo
import org.slade.chase.ui.minDimension
import org.slade.chase.ui.moveTo
import org.slade.chase.ui.progress.SpeedoMeterConfig
import org.slade.chase.ui.theme.ChaseTheme
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.floor

data class Tick(
    var length: Float,
    var thickness: Float,
    var interval: Float
)

@Composable
actual fun NetworkMonitor(
    value: Float,
    modifier: Modifier,
    config: SpeedoMeterConfig
) {
    val primaryColor = ChaseTheme.colors.primary

    val textMeasurer = rememberTextMeasurer()

    val meterValue by animateFloatAsState(
        targetValue = value,
        animationSpec = tween(
            durationMillis = 1000,
            easing = LinearEasing
        )
//        animationSpec = spring(
//            dampingRatio = Spring.DampingRatioHighBouncy,
//            stiffness = Spring.StiffnessLow
//        )
    )

    Canvas(
        modifier = Modifier
            .background(color = config.background)
            .clip(RectangleShape)
            .then(modifier)
    ) {
        val sweepAngle = config.sweepAngle
        val height = size.height
        val width = size.width
        val startAngle = (360f - sweepAngle).let { unused ->
            90f + ((unused/2f))
        }
        val sweepRange = (startAngle..(startAngle+sweepAngle))

        val scaleRadius = center.minDimension * 0.85f
        val h = when(sweepAngle <= 180) {
            true -> 0f
            else -> scaleRadius * cos(Math.toRadians((360.0 - sweepAngle) / 2).toFloat())
        }

        val scaleHeight = scaleRadius + h
        val diff = (scaleRadius - (scaleHeight / 2))

        val centerOffset = Offset(width * 0.5f, center.y + diff )

        val tickAngle = 10f
        // Theta / 360 (2 * PI * R)
        val circumference = 2 * PI * scaleRadius
        val tickLength = (tickAngle / 360f) * circumference

        val revolution = floor(meterValue/circumference).toInt()
        val rotationAngle = -(meterValue % 360f)

        val majorTick = Tick(
            length = 24f,
            thickness = 2f,
            interval = 10f
        )
        val minorTick = Tick(
            length = 16f,
            thickness = 2f,
            interval = 2f
        )

        rotate(degrees = rotationAngle, pivot = centerOffset) {
            val viewPortRanges = when(sweepAngle <= 180) {
                true -> listOf(sweepRange)
                else -> listOf(sweepRange, 0f..(90f - ((360f - sweepAngle)/2f)))
            }
            var angle = 0f
            while (angle < 360f) {
                if(viewPortRanges.some { (abs(rotationAngle + angle) % 360f) in it }) {
                    val isMajor = angle % majorTick.interval == 0f
                    val tickHead = getXY(radius = scaleRadius, center = centerOffset, angle = -angle)
                    val tickTail = getXY(radius = scaleRadius - if(isMajor) majorTick.length else minorTick.length, center = centerOffset, angle = -angle)
                    val tickPath = Path().apply {
                        moveTo(tickHead)
                        lineTo(tickTail)
                    }
                    drawPath(
                        path = tickPath,
                        color = if(isMajor) Color.Cyan else Color.White,
                        style = Stroke(width = if(isMajor) majorTick.thickness else minorTick.thickness, cap = StrokeCap.Round)
                    )
                    if(isMajor) {
                        drawCircle(
                            color = Color.LightGray,
                            center = getXY(radius = scaleRadius + 4f, center = centerOffset, angle = -angle),
                            radius = 2f
                        )
                    }
                }
                angle += minorTick.interval
            }
        }

        val pointer = meterValue + 360f
        val txt = "[ ${(0 until 360 step majorTick.interval.toInt()).joinToString(" ") { "${(pointer - it).toInt()}" }} ]"
        drawText(
            textMeasurer = textMeasurer,
            text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                ) {
                    append(txt)
                }
            },
            maxLines = 1
        )

        drawCircle(
            color = Color.Cyan,
            center = centerOffset,
            radius = 4f,
            style = Stroke(width = 2f)
        )

        drawCircle(
            color = Color.Green,
            center = center,
            radius = 8f,
            style = Stroke(width = 2f)
        )

//        val needleAngle = progress + startAngle
//        val needleLength = scaleRadius * 0.75f
        val scaleOffset = getXY(
            radius = scaleRadius,
            center = centerOffset,
            angle = startAngle
        )

        // Needle pointer
        // val needlePointer = getXY(radius = needleLength, center = centerOffset, angle = needleAngle)
        // Needle base
        // val needleBase = getXY(radius = gap, center = centerOffset, angle = needleAngle)
    }
}