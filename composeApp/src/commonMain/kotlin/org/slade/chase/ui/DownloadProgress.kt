package org.slade.chase.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DownloadProgress() {

    val coroutineScope = rememberCoroutineScope()

    var percentage by remember {
        mutableStateOf(0f)
    }

    val progress by animateFloatAsState(
        targetValue = percentage,
    )

    LaunchedEffect("Progress") {
        coroutineScope.launch {
            do {
                percentage += 1f;
                delay(500)
            } while (percentage < 100)
        }
    }

    Slider(
        value = progress,
        valueRange = 0f..100f,
        onValueChange = {}
    )
}