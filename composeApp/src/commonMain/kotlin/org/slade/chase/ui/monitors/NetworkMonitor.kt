package org.slade.chase.ui.monitors

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.slade.chase.ui.progress.SpeedoMeterConfig

@Composable
expect fun NetworkMonitor(
    value: Float,
    modifier: Modifier = Modifier,
    config: SpeedoMeterConfig = SpeedoMeterConfig()
)