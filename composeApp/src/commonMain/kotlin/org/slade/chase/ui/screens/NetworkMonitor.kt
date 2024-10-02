package org.slade.chase.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.slade.chase.ui.monitors.NetworkMonitor
import org.slade.chase.ui.progress.SpeedoMeterConfig

@Composable
fun Network() {

    var value by remember {
        mutableStateOf(0f)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        NetworkMonitor(
            value = value,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .border(
                    width = 2.dp,
                    color = Color.Yellow
                ),
            config = SpeedoMeterConfig().apply {
                sweepAngle = 270f
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "$value")

        Spacer(modifier = Modifier.height(8.dp))

        Slider(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = {
                value = it
            },
            valueRange = 0f..(360f * 5)
        )
    }
}