package org.slade.chase.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import chase.composeapp.generated.resources.Res
import chase.composeapp.generated.resources.outline_cell_wifi_24
import chase.composeapp.generated.resources.outline_cloud_download_24
import org.jetbrains.compose.resources.vectorResource
import org.slade.chase.ui.screens.downloads.DownloadsScreen

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
        },
        bottomBar = {

            val screens = listOf(
                "Downloads" to DownloadsScreen() to vectorResource(Res.drawable.outline_cloud_download_24),
                "Network" to NetworkMonitorScreen() to vectorResource(Res.drawable.outline_cell_wifi_24),
                "Settings" to SettingsScreen() to Icons.Outlined.Settings
            )

            BottomAppBar(
                actions = {
                    screens.map { (sc, icon) ->
                        val (title, screen) = sc
                        NavigationBarItem(
                            selected = navigator.lastItem.key == screen.key,
                            onClick = {
                                navigator.push(screen)
                            },
                            icon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = title
                                )
                            },
                            label = {
                                Text(
                                    text = title
                                )
                            }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            content()
        }
    }
}