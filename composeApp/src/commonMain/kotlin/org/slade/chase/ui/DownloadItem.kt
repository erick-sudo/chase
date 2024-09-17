package org.slade.chase.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import chase.composeapp.generated.resources.Res
import chase.composeapp.generated.resources.compose_multiplatform
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.slade.chase.models.DownloadItem

@Composable
fun DownloadItem() {

    val coroutineScope = rememberCoroutineScope()

    val contentLength = 323242f

    var bytesRead by remember {
        mutableStateOf(0f)
    }

    val bytesReadAnimatedState by animateFloatAsState(
        targetValue = bytesRead,
        animationSpec = tween(100)
    )

    LaunchedEffect("DownloadJob") {
        coroutineScope.launch {
            var bytes = 0f
            do {
                bytes += (0..1000).random().toFloat().also { bytesRead += (if(bytesRead + it > contentLength) contentLength - bytesRead else it)  }
                delay(100)
            } while (bytes <= contentLength)
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        //File type icon
        Icon(
            modifier = Modifier
                .size(50.dp),
            painter = painterResource(Res.drawable.compose_multiplatform) ,
            contentDescription = "File type"
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "He sells sea shells at the sea shore.",
            )

            Text(
                text = "$bytesReadAnimatedState/$contentLength",
                textAlign = TextAlign.Right,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Slider(
                valueRange = 0f..contentLength,
                value = bytesReadAnimatedState,
                onValueChange = {},
                enabled = false,
            )
        }
    }
}

@Preview
@Composable
fun DownloadItemPreview() {
    MaterialTheme {
        DownloadItem()
    }
}