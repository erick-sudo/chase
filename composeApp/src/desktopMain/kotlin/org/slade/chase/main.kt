package org.slade.chase

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.slade.chase.ui.ChaseWindow

fun main() = application {

    ChaseWindow(
        onCloseRequest = this::exitApplication,
        title = "Chase"
    ) {
        //App()
    }

//    Window(
//        onCloseRequest = ::exitApplication,
//        title = "Chase",
//    ) {
//        App()
//    }
}
