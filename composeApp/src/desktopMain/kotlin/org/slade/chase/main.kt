package org.slade.chase

import androidx.compose.ui.window.application
import org.slade.chase.ui.ChaseWindow

fun main() = application {

    ChaseWindow(
        onCloseRequest = this::exitApplication,
        title = "Chase"
    ) {
        App()
    }
}
