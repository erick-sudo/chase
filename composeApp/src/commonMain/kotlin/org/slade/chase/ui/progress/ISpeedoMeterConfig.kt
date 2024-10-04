package org.slade.chase.ui.progress

import androidx.compose.ui.graphics.Color

interface ISpeedoMeterConfig {
    var background: Color
    var trackColor: Color
    var needleSectorAngle: Float
    var sweepAngle: Float
    var needleBaseGap: Float
    var needleBaseRadius: Float
    var progressStrokeWidth: Float
    var majorTick: Tick
    var minorTick: Tick
}