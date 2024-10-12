package org.slade.chase.ui.progress

import androidx.compose.ui.graphics.Color

class SpeedoMeterConfig : ISpeedoMeterConfig {
    override var background: Color = Color.Transparent
    override var trackColor: Color = Color.Black.copy(alpha = 0.125f)
    override var needleSectorAngle: Float = 30f
    override var sweepAngle: Float = 240f
    override var needleBaseGap: Float = 1f
    override var needleBaseRadius: Float = 1f
    override var progressStrokeWidth: Float = 4f
    override var majorTick: Tick = Tick(
        length = 4f,
        thickness = 2f,
        interval = 10f,
        color = Color.Unspecified
    )
    override var minorTick: Tick = Tick(
        length = 2f,
        thickness = 2f,
        interval = 2f,
        color = Color.Unspecified
    )
}