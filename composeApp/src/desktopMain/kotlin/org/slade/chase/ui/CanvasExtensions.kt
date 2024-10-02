package org.slade.chase.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

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

fun Double.closestMultiple(divisor: Double): Double {
    val remainder = this % divisor
    if(remainder == 0.0) {
        return this
    }
    val isAboveOrEqualToHalfTheDivisor = remainder >= divisor / 2
    return  this + when(isAboveOrEqualToHalfTheDivisor) {
        true -> - remainder
        else -> divisor - remainder
    }
}