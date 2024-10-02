package org.slade.chase

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.slade.chase.ui.ChaseWindow

fun main() = application {

    val windowState = rememberWindowState(
        size = DpSize(
            700.dp,
            400.dp
        ),
        position = WindowPosition(
            alignment = Alignment.Center
        ),
        placement = WindowPlacement.Maximized
    )

    ChaseWindow(
        onCloseRequest = this::exitApplication,
        state = windowState
    ) {
        App()
    }
}
