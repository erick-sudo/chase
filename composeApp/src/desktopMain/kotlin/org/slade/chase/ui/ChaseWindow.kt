package org.slade.chase.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberWindowState
import org.slade.chase.ui.theme.ChaseTheme

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
        transparent = true,
        undecorated = true,
        resizable = resizable,
        enabled = enabled,
        focusable = focusable,
        alwaysOnTop = alwaysOnTop,
        onPreviewKeyEvent = onPreviewKeyEvent,
        onKeyEvent = onKeyEvent,
    ) {
        ChaseTheme {
            Surface (
                modifier = Modifier
                    .fillMaxSize(),
                border = BorderStroke(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = ChaseTheme.borderGradientColors
                    )
                )
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .height(36.dp)
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceContainer)
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
                        }

                        // Window control icons
                        Row(
                            modifier = Modifier
                                .fillMaxHeight()
                        ) {
                            FilledTonalIconButton(
                                modifier = Modifier
                                    .size(36.dp),
                                shape = RectangleShape,
                                colors = IconButtonDefaults.outlinedIconButtonColors(),
                                onClick = {
                                    onCloseRequest()
                                }
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .size(16.dp),
                                    imageVector = Icons.Outlined,
                                    contentDescription = "Minimize"
                                )
                            }
                            FilledTonalIconButton(
                                modifier = Modifier
                                    .size(36.dp),
                                shape = RectangleShape,
                                colors = IconButtonDefaults.outlinedIconButtonColors(),
                                onClick = {
                                    onCloseRequest()
                                }
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .size(16.dp),
                                    imageVector = Icons.Outlined.Face,
                                    contentDescription = "Maximize"
                                )
                            }
                            FilledTonalIconButton(
                                modifier = Modifier
                                    .size(36.dp),
                                shape = RectangleShape,
                                colors = IconButtonDefaults.outlinedIconButtonColors(),
                                onClick = {
                                    onCloseRequest()
                                }
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .size(16.dp),
                                    imageVector = Icons.Outlined.Close,
                                    contentDescription = "Close"
                                )
                            }
                        }
                    }

                    // Main window content
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
//                    content()
                    }
                }
            }
        }
    }
}

