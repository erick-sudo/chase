package org.slade.chase.ui.progress

import androidx.compose.ui.graphics.Color

data class Tick(
    var length: Float,
    var thickness: Float,
    var interval: Float,
    var color: Color
)
