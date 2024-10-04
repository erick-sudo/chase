package org.slade.chase.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberWindowState
import chase.composeapp.generated.resources.Res
import chase.composeapp.generated.resources.close_window
import chase.composeapp.generated.resources.maximize_window
import chase.composeapp.generated.resources.minimize_window
import chase.composeapp.generated.resources.restore_window
import org.jetbrains.compose.resources.vectorResource
import org.slade.chase.ui.theme.ChaseTheme

@Composable
fun ChaseWindow(
    onCloseRequest: () -> Unit,
    state: WindowState = rememberWindowState(),
    visible: Boolean = true,
    icon: Painter? = null,
    resizable: Boolean = true,
    enabled: Boolean = true,
    focusable: Boolean = true,
    alwaysOnTop: Boolean = false,
    onPreviewKeyEvent: (KeyEvent) -> Boolean = { false },
    onKeyEvent: (KeyEvent) -> Boolean = { false },
    toolBar: @Composable (RowScope.() -> Unit) = { },
    content: @Composable (BoxScope.() -> Unit)
) {

    val isVisible by remember {
        mutableStateOf(visible)
    }

    Window(
        onCloseRequest = onCloseRequest,
        state = state,
        visible = isVisible,
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
                        colors = when(state.placement == WindowPlacement.Maximized) {
                            true -> listOf(Color.Transparent, Color.Transparent)
                            else -> ChaseTheme.borderGradientColors.map { it.copy(alpha = 0.5f) }
                        }
                    )
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    WindowDraggableArea(
                        modifier = Modifier
                            .height(36.dp)
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
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
                                ChaseTheme {
                                    toolBar()
                                }
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
                                        state.isMinimized = true
                                    }
                                ) {
                                    Icon(
                                        modifier = Modifier
                                            .size(16.dp),
                                        imageVector = vectorResource(Res.drawable.minimize_window),
                                        contentDescription = "Minimize"
                                    )
                                }

                                if(resizable) {
                                    FilledTonalIconButton(
                                        modifier = Modifier
                                            .size(36.dp),
                                        shape = RectangleShape,
                                        colors = IconButtonDefaults.outlinedIconButtonColors(),
                                        onClick = {
                                            when(state.placement) {
                                                WindowPlacement.Maximized -> state.placement = WindowPlacement.Floating
                                                else -> state.placement = WindowPlacement.Maximized
                                            }
                                        }
                                    ) {
                                        Crossfade(
                                            targetState = state.placement
                                        ) { windowPlacement ->
                                            when(windowPlacement) {
                                                WindowPlacement.Maximized -> Icon(
                                                    modifier = Modifier
                                                        .size(16.dp),
                                                    imageVector = vectorResource(Res.drawable.restore_window),
                                                    contentDescription = "Restore"
                                                )
                                                else -> Icon(
                                                    modifier = Modifier
                                                        .size(16.dp),
                                                    imageVector = vectorResource(Res.drawable.maximize_window),
                                                    contentDescription = "Maximize"
                                                )
                                            }
                                        }
                                    }
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
                                        imageVector = vectorResource(Res.drawable.close_window),
                                        contentDescription = "Close"
                                    )
                                }
                            }
                        }
                    }

                    // Main window content
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        content()
                    }
                }
            }
        }
    }
}

