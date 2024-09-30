package org.slade.chase.ui.progress

data class CircularProgressSegment(
    var offsetAngle: Float,
    var sweepAngle: Float,
    var windowAngle: Float
)

data class LinearProgressSection(
    var offset: Float,
    var progress: Float,
    var window: Float,
)
