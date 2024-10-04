package org.slade.chase.ui.progress

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.slade.chase.ui.theme.ChaseTheme

@Composable
expect fun SpeedoMeter(
    modifier: Modifier = Modifier,
    config: SpeedoMeterConfig = SpeedoMeterConfig(),
    value: Float = 0f,
    progressColors: List<Color> = ChaseTheme.borderGradientColors.reversed()
)