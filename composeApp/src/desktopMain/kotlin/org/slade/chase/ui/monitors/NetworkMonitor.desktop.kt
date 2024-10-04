package org.slade.chase.ui.monitors

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.withStyle
import org.slade.chase.ui.getXY
import org.slade.chase.ui.lineTo
import org.slade.chase.ui.minDimension
import org.slade.chase.ui.moveTo
import org.slade.chase.ui.progress.SpeedoMeterConfig
import kotlin.math.cos

@Composable
actual fun NetworkMonitor(
    pointerPercentage: Float,
    value: Float,
    modifier: Modifier,
    config: SpeedoMeterConfig
) {

    val textMeasurer = rememberTextMeasurer()

    val meterValue by animateFloatAsState(
        targetValue = value,
//        animationSpec = tween(
//            durationMillis = 1000,
//            easing = LinearEasing
//        ),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    val progress by animateFloatAsState(
        targetValue = pointerPercentage,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessLow
        )
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

        val scaleRadius = center.minDimension * 0.95f
        val h = when(sweepAngle <= 180) {
            true -> 0f
            else -> scaleRadius * cos(Math.toRadians((360.0 - sweepAngle) / 2).toFloat())
        }

        val scaleHeight = scaleRadius + h
        val diff = (scaleRadius - (scaleHeight / 2))

        val centerOffset = Offset(width * 0.5f, center.y + diff )

        val rotationAngle = (meterValue % 360f)

        val majorTick = config.majorTick
        val minorTick = config.minorTick

        val needleAngle = ((progress/100f) * sweepAngle) + startAngle
        val needleLength = scaleRadius - majorTick.length
        val scaleOffset = getXY(
            radius = scaleRadius,
            center = centerOffset,
            angle = startAngle
        )
        // Needle pointer
        val needlePointer = getXY(radius = needleLength, center = centerOffset, angle = needleAngle)
        val needleBaseLeft = getXY(radius = config.needleBaseRadius, center = centerOffset, angle = needleAngle - 90f)
        val needlePath = Path().apply {
            moveTo(needlePointer)
            lineTo(needleBaseLeft)
            arcTo(
                rect = Rect(radius = config.needleBaseRadius, center = centerOffset),
                startAngleDegrees = needleAngle - 90f,
                sweepAngleDegrees = -180f,
                forceMoveTo = true,
            )
            lineTo(needlePointer)
        }

        drawCircle(
            center = centerOffset,
            brush = Brush.radialGradient(
                colors = listOf(config.majorTick.color.copy(alpha = 0.2f), Color.Transparent, Color.Transparent),
                center = centerOffset,
            ),
            radius = scaleRadius
        )

//        drawText(
//            textMeasurer = textMeasurer,
//            text = buildAnnotatedString {
//                withStyle(SpanStyle(
//                    color = Color.White
//                )) {
//                    append("Major: $majorTick\nMinor: $minorTick")
//                }
//            }
//        )

        rotate(degrees = rotationAngle, pivot = centerOffset) {
            val viewPortRanges = when(sweepAngle <= 180) {
                true -> listOf(sweepRange)
                else -> listOf(sweepRange, 0f..(90f - ((360f - sweepAngle)/2f)))
            }
            var angle = 0f
            while (angle < 360f) {
                if(viewPortRanges.any { ((rotationAngle + angle) % 360f) in it }) {
                    val isMajor = angle % majorTick.interval == 0f
                    val tickHead = getXY(radius = if(isMajor) scaleRadius - 2f else scaleRadius, center = centerOffset, angle = angle)
                    val tickTail = getXY(radius = scaleRadius - if(isMajor) majorTick.length else minorTick.length, center = centerOffset, angle = angle)
                    val tickPath = Path().apply {
                        moveTo(tickHead)
                        lineTo(tickTail)
                    }
                    drawPath(
                        path = tickPath,
                        color = if(isMajor) majorTick.color else minorTick.color,
                        style = Stroke(width = if(isMajor) majorTick.thickness else minorTick.thickness, cap = StrokeCap.Round)
                    )
                    if(isMajor) {
                        drawCircle(
                            color = config.minorTick.color,
                            center = getXY(radius = scaleRadius + 4f, center = centerOffset, angle = angle),
                            radius = 2f
                        )

                        /* val labelOffset = getXY(radius = scaleRadius + 24f, center = centerOffset, angle = angle)

                        rotate(degrees = angle + 90f, pivot = labelOffset) {
                            translate(
                                left = -18f
                            ) {
                                try {
                                    drawText(
                                        topLeft = labelOffset,
                                        size = Size(width = 36f, height = 16f),
                                        textMeasurer = textMeasurer,
                                        text = buildAnnotatedString {
                                            withStyle(
                                                SpanStyle(
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Thin,
                                                    fontSize = 16.sp
                                                )
                                            ) {
                                                append("${(angle+meterValue).toLong()}")
                                            }
                                        },
                                        maxLines = 1
                                    )
                                } catch (e: Exception) {
                                    // Ignore
                                }
                            }
                        } */
                    }
                }
                angle += minorTick.interval
            }
        }

        drawPath(
            path = needlePath,
            color = config.minorTick.color,
        )

        rotate(degrees = 45f, pivot = centerOffset) {
            drawPath(
                path = Path().apply {
                    moveTo(getXY(radius = config.needleBaseRadius * 0.5f, center = centerOffset, angle = needleAngle - 45f))
                    lineTo(getXY(radius = config.needleBaseRadius * 0.5f, center = centerOffset, angle = needleAngle - 135f))
                    lineTo(getXY(radius = config.needleBaseRadius * 0.5f, center = centerOffset, angle = needleAngle + 135f))
                    lineTo(getXY(radius = config.needleBaseRadius * 0.5f, center = centerOffset, angle = needleAngle + 45f))
                    close()
                },
                color = config.majorTick.color,
            )
        }
    }
}