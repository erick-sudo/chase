package org.slade.chase

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.slade.chase.ui.ChaseWindow

fun main() = application {

    val windowState = rememberWindowState(
        size = DpSize(
            700.dp,
            400.dp
        )
    )

    ChaseWindow(
        onCloseRequest = this::exitApplication,
        state = windowState
    ) {
        App()
    }
}
