package org.slade.chase.ui.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.rememberWindowState
import org.slade.chase.ui.ChaseWindow
import org.slade.chase.ui.theme.ChaseTheme

@Composable
actual fun DownloadCompleteDialog(
    modifier: Modifier,
    visible: Boolean,
    onCloseRequest: () -> Unit
) {

    val windowState = rememberWindowState(
        width = 360.dp,
        height = 256.dp,
    )

    ChaseTheme {
        ChaseWindow(
            state = windowState,
            visible = visible,
            onCloseRequest = onCloseRequest,
            resizable = false,
            showTopBar = false,
            borderGradientColors = listOf(
                Color(0, 120, 0),
                Color(0, 80, 0),
                Color(0, 120, 0),
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Download Completed Successfully",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0, 165, 0),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Icon(
                    modifier = Modifier
                        .size(64.dp),
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = "Success",
                    tint = Color(0, 165, 0)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "robbins.pdf",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f),
                    color = Color(0, 120, 0),
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    ElevatedButton(
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = Color(0, 120, 0, 20),
                            contentColor = Color(0, 120, 0)
                        ),
                        modifier = Modifier
                            .weight(1f),
                        onClick = {
                            onCloseRequest()
                        }
                    ) {
                        Text(
                            text = "OK",
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    ElevatedButton(
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = Color(0, 120, 0, 20),
                            contentColor = Color(0, 120, 0)
                        ),
                        modifier = Modifier
                            .weight(1f),
                        onClick = {
                            onCloseRequest()
                        }
                    ) {
                        Text(
                            text = "Open Location",
                            maxLines = 1,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}