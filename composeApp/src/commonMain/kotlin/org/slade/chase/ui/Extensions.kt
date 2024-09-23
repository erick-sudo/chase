package org.slade.chase.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

fun DrawScope.drawDoughnut(
    center: Offset,
    radius: Float,
    startAngle: Float,
    sweepAngle: Float,
    strokeWidth: Float,
    color: Color
) {
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(width = strokeWidth),
        topLeft = Offset(
            x = center.x - radius,
            y = center.y - radius
        ),
        size = Size(
            width = radius * 2,
            height = radius * 2
        )
    )
}

fun DrawScope.drawDoughnut(
    center: Offset,
    radius: Float,
    startAngle: Float,
    sweepAngle: Float,
    strokeWidth: Float,
    brush: Brush
) {
    drawArc(
        brush = brush,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(width = strokeWidth),
        topLeft = Offset(
            x = center.x - radius,
            y = center.y - radius
        ),
        size = Size(
            width = radius * 2,
            height = radius * 2
        )
    )
}