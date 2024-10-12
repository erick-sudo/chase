package org.slade.chase.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import chase.composeapp.generated.resources.Res
import chase.composeapp.generated.resources.outline_cell_wifi_24
import chase.composeapp.generated.resources.outline_cloud_download_24
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.vectorResource
import org.slade.chase.ChaseRichToolTip
import org.slade.chase.ui.screens.downloads.DownloadsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun MainContentHostLayout(
    modifier: Modifier,
    drawerState: DrawerState,
    navigator: Navigator,
    content: @Composable BoxScope.() -> Unit
) {

    val coroutineScope = rememberCoroutineScope()

    Scaffold (
        modifier = modifier,
        floatingActionButton = {
            Button(
                onClick = {

                },
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "New Download"
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("New")
            }
        }
    ) { paddingValues ->

        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .border(1.dp, MaterialTheme.colorScheme.background)
                    .padding(4.dp),
                verticalArrangement = Arrangement.Bottom
            ) {

                TextButton(
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.height(36.dp).width(36.dp),
                    shape = RoundedCornerShape(8.dp),
                    onClick = {
                        coroutineScope.launch {
                            drawerState.open()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Menu,
                        contentDescription = "Drawer Toggle"
                    )
                }

                Spacer(
                    modifier = Modifier
                        .weight(1f)
                )

                ChaseRichToolTip(
                    tooltip = {
                        RichTooltip { Text("Downloads") }
                    }
                ) {
                    TextButton(
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.height(36.dp).width(36.dp),
                        shape = RoundedCornerShape(8.dp),
                        onClick = {
                            navigator.push(DownloadsScreen())
                        }
                    ) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.outline_cloud_download_24),
                            contentDescription = "Downloads"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                ChaseRichToolTip(
                    tooltip = {
                        RichTooltip { Text("Network") }
                    }
                ) {
                    TextButton(
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.height(36.dp).width(36.dp),
                        shape = RoundedCornerShape(8.dp),
                        onClick = {
                            navigator.push(NetworkMonitorScreen())
                        }
                    ) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.outline_cell_wifi_24),
                            contentDescription = "Network"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                ChaseRichToolTip(
                    tooltip = {
                        RichTooltip { Text("Settings") }
                    }
                ) {
                    TextButton(
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.height(36.dp).width(36.dp),
                        shape = RoundedCornerShape(8.dp),
                        onClick = {
                            navigator.push(SettingsScreen())
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .padding(paddingValues)
            ) {
                content()
            }
        }
    }
}