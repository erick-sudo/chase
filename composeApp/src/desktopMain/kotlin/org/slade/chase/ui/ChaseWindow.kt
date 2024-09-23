package org.slade.chase.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberWindowState

@Composable
fun ChaseWindow(
    onCloseRequest: () -> Unit,
    state: WindowState = rememberWindowState(),
    visible: Boolean = true,
    title: String = "Chase",
    icon: Painter? = null,
    resizable: Boolean = true,
    enabled: Boolean = true,
    focusable: Boolean = true,
    alwaysOnTop: Boolean = false,
    onPreviewKeyEvent: (KeyEvent) -> Boolean = { false },
    onKeyEvent: (KeyEvent) -> Boolean = { false },
    toolBar: @Composable (RowScope.() -> Unit) = { },
    content: @Composable (ColumnScope.() -> Unit)
) {
    Window(
        onCloseRequest = onCloseRequest,
        state = state,
        visible = visible,
        title = title,
        undecorated = true,
        resizable = resizable,
        enabled = enabled,
        focusable = focusable,
        alwaysOnTop = alwaysOnTop,
        onPreviewKeyEvent = onPreviewKeyEvent,
        onKeyEvent = onKeyEvent,
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Top app bar
            Row(
               modifier = Modifier
                   .height(40.dp)
                   .fillMaxWidth()
            ) {

                // Window Icon
                if(icon != null) {
                    Box(
                        modifier = Modifier,
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = icon,
                            contentDescription = "Icon"
                        )
                    }
                }

                // Toolbar items
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    toolBar()

                    Text("Color: $color")
                }

                // Window control icons
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                ) {
                    Button(
                        modifier = Modifier,
                        elevation = null,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Transparent,
                            contentColor = Color.DarkGray,
                        ),
                        shape = RectangleShape,
                        onClick = {
                            onCloseRequest()
                        }

                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "Close window"
                        )
                    }
                }
            }

            // Main window content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(5.dp, Color.Magenta)
            ) {
                content()
            }
        }
    }
}