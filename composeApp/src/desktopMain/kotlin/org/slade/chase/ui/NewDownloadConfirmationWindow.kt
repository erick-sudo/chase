package org.slade.chase.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.rememberWindowState
import org.slade.chase.models.DownloadItem

@Composable
internal fun NewDownloadConfirmation(
    url: String,
    onSuccess: (DownloadItem) -> Unit,
    recreate: () -> Unit,
    onCancel: () -> Unit
) {

    val windowState = rememberWindowState(
        width = 480.dp,
        height = 360.dp
    )

//    LaunchedEffect("Initialize") {
//        val downloadItem = DownloadItem.init(url)
//    }

    ChaseWindow(
        toolBar = {
            TextButton(
                modifier = Modifier,
                shape = RectangleShape,
                onClick = {
                    recreate()
                }
            ) {
                Text(text = "Recreate")
            }
        },
        onCloseRequest = {
            onCancel()
        },
        resizable = false,
        state = windowState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            CircularProgressIndicator(
                modifier = Modifier
                    .weight(1f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                ElevatedButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    onClick = {
                        onCancel()
                    },
                    shape = RectangleShape
                ) {
                    Text("Cancel")
                }

                ElevatedButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    onClick = {
                        // val downloadItem = DownloadItem(newDownloadUrl)
                    },
                    shape = RectangleShape
                ) {
                    Text("Queue")
                }

                ElevatedButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    onClick = {
                        // val downloadItem = DownloadItem(newDownloadUrl)
                    },
                    shape = RectangleShape
                ) {
                    Text("Start Now")
                }
            }
        }
    }
}