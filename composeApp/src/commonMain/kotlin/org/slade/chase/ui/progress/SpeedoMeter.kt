package org.slade.chase.ui.progress

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.slade.chase.ui.theme.ChaseTheme

interface ISpeedoMeterConfig {
    var background: Color
    var trackColor: Color
    var needleSectorAngle: Float
    var sweepAngle: Float
    var needleBaseGap: Float
    var needleBaseRadius: Float
    var progressStrokeWidth: Float
}

expect class SpeedoMeterConfig(): ISpeedoMeterConfig

@Composable
expect fun SpeedoMeter(
    modifier: Modifier = Modifier,
    config: SpeedoMeterConfig = SpeedoMeterConfig(),
    value: Float = 0f,
    progressColors: List<Color> = ChaseTheme.borderGradientColors.reversed()
)