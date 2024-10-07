package org.slade.chase.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import chase.composeapp.generated.resources.Res
import chase.composeapp.generated.resources.outline_file_download_24
import chase.composeapp.generated.resources.outline_file_upload_24
import org.jetbrains.compose.resources.vectorResource
import org.slade.chase.ui.dialogs.DownloadCompleteDialog
import org.slade.chase.ui.monitors.NetworkMonitor
import org.slade.chase.ui.progress.SpeedoMeterConfig
import org.slade.chase.ui.progress.Tick
import org.slade.chase.ui.theme.ChaseTheme

class NetworkMonitorScreen : Screen {

    override val key: ScreenKey
        get() = super.key

    @Composable
    override fun Content() {
        
        var showDownloadCompleteDialog by remember {
            mutableStateOf(false)
        }

        if(showDownloadCompleteDialog) {
            DownloadCompleteDialog(
                visible = showDownloadCompleteDialog,
                onCloseRequest = {
                    showDownloadCompleteDialog = false
                }
            )
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Row(
                modifier = Modifier
                    .padding(16.dp)
            ) {

                DownloadSpeed(
                    modifier = Modifier
                        .weight(1f)
                )

                Spacer(modifier = Modifier.width(16.dp))

                UploadSpeed(
                    modifier = Modifier
                        .weight(1f)
                )
            }

            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Button(
                    onClick = {
                        showDownloadCompleteDialog = true
                    }
                ) {
                    Text(
                        text = "Open Download Complete Dialog"
                    )
                }
            }
        }
    }
}

@Composable
private fun DownloadSpeed(
    modifier: Modifier = Modifier
) {

    val primaryColor = ChaseTheme.colors.primary
    val majorTickColor = Color(0, 80, 10)
    val minorTickColor = Color(0, 120, 10)

    var value by remember {
        mutableStateOf(0f)
    }

    var needle by remember {
        mutableStateOf(0f)
    }

    Column(
        modifier = Modifier
            .border(
                color = majorTickColor,
                width = 1.dp,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(16.dp)
            .then(modifier)
    ) {

        Row(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Icon(
                imageVector = vectorResource(Res.drawable.outline_file_download_24),
                contentDescription = "Download",
                tint = majorTickColor
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Download Speed",
                color = majorTickColor
            )
        }

        NetworkMonitor(
            pointerPercentage = needle,
            value = value,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            config = SpeedoMeterConfig().apply {
                sweepAngle = 240f
                needleBaseRadius = 4f
                minorTick = Tick(
                    length = 16f,
                    thickness = 1f,
                    interval = 2f,
                    color = minorTickColor
                )
                majorTick = Tick(
                    length = 24f,
                    thickness = 1.5f,
                    interval = 10f,
                    color = majorTickColor
                )
            }
        )

//        Spacer(modifier = Modifier.height(8.dp))
//
//        Slider(
//            modifier = Modifier.fillMaxWidth(),
//            value = needle,
//            onValueChange = {
//                needle = it
//            },
//            valueRange = 0f..100f
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Slider(
//            modifier = Modifier.fillMaxWidth(),
//            value = value,
//            onValueChange = {
//                value = it
//            },
//            valueRange = 0f..(360f * 5)
//        )
    }
}

@Composable
private fun UploadSpeed(
    modifier: Modifier = Modifier
) {

    val primaryColor = ChaseTheme.colors.primary
    val majorTickColor = MaterialTheme.colorScheme.primaryContainer
    val minorTickColor = MaterialTheme.colorScheme.tertiary

    var value by remember {
        mutableStateOf(0f)
    }

    var needle by remember {
        mutableStateOf(0f)
    }

    Column (
        modifier = Modifier
            .border(
                color = majorTickColor,
                width = 1.dp,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(16.dp)
            .then(modifier)
    ) {

        Row(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Icon(
                imageVector = vectorResource(Res.drawable.outline_file_upload_24),
                contentDescription = "Upload",
                tint = majorTickColor
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Upload Speed",
                color = majorTickColor
            )
        }

        NetworkMonitor(
            pointerPercentage = needle,
            value = value,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            config = SpeedoMeterConfig().apply {
                sweepAngle = 240f
                needleBaseRadius = 4f
                minorTick = Tick(
                    length = 16f,
                    thickness = 1f,
                    interval = 2f,
                    color = minorTickColor
                )
                majorTick = Tick(
                    length = 24f,
                    thickness = 1.5f,
                    interval = 10f,
                    color = majorTickColor
                )
            }
        )

//        Spacer(modifier = Modifier.height(8.dp))
//
//        Slider(
//            modifier = Modifier.fillMaxWidth(),
//            value = needle,
//            onValueChange = {
//                needle = it
//            },
//            valueRange = 0f..100f
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Slider(
//            modifier = Modifier.fillMaxWidth(),
//            value = value,
//            onValueChange = {
//                value = it
//            },
//            valueRange = 0f..(360f * 5)
//        )
    }
}