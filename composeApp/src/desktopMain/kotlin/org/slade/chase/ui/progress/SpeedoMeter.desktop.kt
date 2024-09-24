package org.slade.chase.ui.progress

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.dp
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tanh

@Composable
actual fun SpeedoMeter(
    modifier: Modifier
) {

    var meter by remember {
        mutableStateOf(0f)
    }

    val meterValue by animateFloatAsState(
        targetValue = meter,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Canvas(
        modifier = Modifier
            .then(modifier)
    ) {
        val sweepAngle = 240f
        val height = size.height
        val width = size.width
        val startAngle = 150f

        val centerOffset = Offset(width / 2f, height / 2.09f)

        val needleAngle = (meterValue / 100f) * sweepAngle + startAngle
        val needleLength = 160f
        val needleBaseRadius = 24f

        // Top point of needle
        val top = getXY(radius = needleLength, center = centerOffset, angle = needleAngle)

        // Base points
        val baseLeft = getXY(radius = needleBaseRadius, center = centerOffset, angle = needleAngle - 35f)
        val baseRight = getXY(radius = needleBaseRadius, center = centerOffset, angle = needleAngle + 35f)

        val shiftedBaseLeft = getXY(radius = needleBaseRadius + 8f, center = centerOffset, angle = needleAngle - 35f)
        val shiftedBaseRight = getXY(radius = needleBaseRadius + 8f, center = centerOffset, angle = needleAngle + 35f)

        drawCircle(
            color = Color.Cyan,
            radius = 4f,
            center = shiftedBaseLeft,
            style = Stroke(width = 2f)
        )

        val needlePath = Path().apply {
            moveTo(top)
            lineTo(baseLeft)
            lineTo(centerOffset)
            lineTo(baseRight)
            close()
        }

        val basePath = Path().apply {
            moveTo(baseLeft)
            arcTo(
                rect = Rect(
                    center = centerOffset,
                    radius = 36f,
                ),
                startAngleDegrees = needleAngle + 35f,
                sweepAngleDegrees = 290f,
                forceMoveTo = true
            )
            lineTo(centerOffset)
            close()
        }

//        scale(
//            scale = 1.2f,
//            pivot = centerOffset
//        ) {
//            drawPath(
//                path = basePath,
//                color = Color.Yellow,
//                style = Stroke(width = 1f)
//            )
//        }

        drawPath(
            path = basePath,
            color = Color.Yellow,
            style = Stroke(width = 1f)
        )

        drawPath(
            path = needlePath,
            color = Color.Black,
            style = Stroke(width = 1f)
        )
    }

    Spacer(modifier = Modifier.height(20.dp))

    Slider(
        modifier = Modifier.width(400.dp),
        value = meter,
        valueRange = 0f..100f,
        onValueChange = { newValue ->
            meter = newValue
        }
    )
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