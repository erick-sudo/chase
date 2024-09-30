package org.slade.chase.ui.progress

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import org.slade.chase.ui.theme.ChaseTheme
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

actual class SpeedoMeterConfig : ISpeedoMeterConfig {
    override var background: Color = Color.Transparent
    override var trackColor: Color = Color.Black.copy(alpha = 0.125f)
    override var needleSectorAngle: Float = 30f
    override var sweepAngle: Float = 240f
    override var needleBaseGap: Float = 1f
    override var needleBaseRadius: Float = 1f
    override var progressStrokeWidth: Float = 4f
}

@Composable
actual fun SpeedoMeter(
    modifier: Modifier,
    config: SpeedoMeterConfig,
    value: Float,
    progressColors: List<Color>
) {

    val primaryColor = ChaseTheme.colors.primary

    val meterValue by animateFloatAsState(
        targetValue = value,
//        animationSpec = spring(
//            dampingRatio = Spring.DampingRatioHighBouncy,
//            stiffness = Spring.StiffnessLow
//        )
        animationSpec = tween(
            durationMillis = 200,
            easing = LinearOutSlowInEasing
        )
    )

    Canvas(
        modifier = Modifier
            .background(color = config.background)
            .clip(RectangleShape)
            .then(modifier)
    ) {
        val backgroundColor = config.background
        val trackColor = config.trackColor
        val sweepAngle = config.sweepAngle
        val height = size.height
        val width = size.width
        val startAngle = (360f - sweepAngle).let { unused ->
            90f + ((unused/2f))
        }
        val needleBaseRadius = config.needleBaseRadius
        val needleSectorAngle = config.needleSectorAngle

        val halfStrokeWidth = (config.progressStrokeWidth / 2)

        val gap = config.needleBaseGap
        val scaleRadius = center.minDimension - halfStrokeWidth
        val h = when(sweepAngle <= 180) {
            true -> 0f
            else -> scaleRadius * cos(Math.toRadians((360.0 - sweepAngle) / 2).toFloat())
        }

        val centerOffset = Offset(width * 0.5f, height - h - halfStrokeWidth)

        val progress = (meterValue / 100f) * sweepAngle

        val needleAngle = progress + startAngle
        val needleLength = scaleRadius * 0.75f
        val scaleOffset = getXY(
            radius = scaleRadius,
            center = centerOffset,
            angle = startAngle
        )

        val bLeft = getXY(radius = needleBaseRadius, center = centerOffset, angle = needleAngle - needleSectorAngle / 2)
        val bRight = getXY(radius = needleBaseRadius, center = centerOffset, angle = needleAngle + needleSectorAngle / 2)

        val chordVector = bLeft - bRight
        val chordMagnitude = sqrt(chordVector.x.pow(2) + chordVector.y.pow(2))
        val chordUnitVector = chordVector / chordMagnitude
        val chordAdvance = (chordUnitVector * gap)

        val needleBaseLeft = bLeft - chordAdvance
        val needleBaseRight = bRight + chordAdvance

        // Needle pointer
        val needlePointer = getXY(radius = needleLength, center = centerOffset, angle = needleAngle)
        // Needle base
        val needleBase = getXY(radius = gap, center = centerOffset, angle = needleAngle)

        val needlePath = Path().apply {
            moveTo(needleBaseLeft)
            lineTo(needlePointer)
            lineTo(needleBaseRight)
            lineTo(needleBase)
            close()
        }

        val basePath = Path().apply {
            moveTo(bLeft)
            arcTo(
                rect = Rect(
                    center = centerOffset,
                    radius = needleBaseRadius,
                ),
                startAngleDegrees = needleAngle + needleSectorAngle / 2,
                sweepAngleDegrees = 360f - needleSectorAngle,
                forceMoveTo = true
            )
            lineTo(centerOffset)
            close()
        }

        val trackPath = Path().apply {
            moveTo(scaleOffset)
            arcTo(
                rect = Rect(
                    center = centerOffset,
                    radius = scaleRadius,
                ),
                startAngleDegrees = startAngle,
                sweepAngleDegrees = sweepAngle,
                forceMoveTo = true
            )
        }

        val progressPath = Path().apply {
            moveTo(scaleOffset)
            arcTo(
                rect = Rect(
                    center = centerOffset,
                    radius = scaleRadius,
                ),
                startAngleDegrees = startAngle,
                sweepAngleDegrees = progress,
                forceMoveTo = true
            )
        }

        drawPath(
            path = basePath,
            brush = Brush.sweepGradient(
                colors = progressColors,
                center = centerOffset
            ),
        )

        drawPath(
            path = needlePath,
            brush = Brush.sweepGradient(
                colors = progressColors,
                center = centerOffset
            ),
        )

        drawPath(
            path = trackPath,
            color = trackColor,
            style = Stroke(
                width = config.progressStrokeWidth,
                cap = StrokeCap.Round
            )
        )

        drawPath(
            path = progressPath,
            brush = Brush.sweepGradient(
                colors = progressColors,
                center = centerOffset
            ),
            style = Stroke(width = config.progressStrokeWidth, cap = StrokeCap.Round)
        )
    }
}

fun Path.build(offsets: List<Offset>) {
    offsets.forEachIndexed { index, offset ->
        if(index == 0) {
            moveTo(offset)
        } else {
            lineTo(offset)
        }
    }
}

fun Path.moveTo(offset: Offset) {
    moveTo(offset.x, offset.y)
}

fun Path.lineTo(offset: Offset) {
    lineTo(offset.x, offset.y)
}

val Offset.minDimension
    get() = min(this.x, this.y)


fun getXY(radius: Float, center: Offset, angle: Float): Offset {
    return Offset(
        x = getX(radius = radius, center = center, angle = angle),
        y = getY(radius = radius, center = center, angle = angle)
    )
}

fun getX(radius: Float, center: Offset, angle: Float): Float {
    return center.x + radius * cos(Math.toRadians(angle.toDouble()).toFloat())
}

fun getY(radius: Float, center: Offset, angle: Float): Float {
    return center.y + radius * sin(Math.toRadians(angle.toDouble()).toFloat())
}