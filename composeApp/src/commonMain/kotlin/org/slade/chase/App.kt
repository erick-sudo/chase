package org.slade.chase

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chase.composeapp.generated.resources.Res
import chase.composeapp.generated.resources.outline_cell_wifi_24
import chase.composeapp.generated.resources.outline_cloud_download_24
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.slade.chase.ui.NewDownload
import org.slade.chase.ui.screens.Downloads
import org.slade.chase.ui.screens.NetworkMonitor
import org.slade.chase.ui.screens.Settings
import org.slade.chase.ui.theme.ChaseTheme

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {

    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState(0) {
        3
    }

    val extensionDrawerState = rememberDrawerState(DrawerValue.Closed)

    var newDownload by remember {
        mutableStateOf(false)
    }

    ChaseTheme {

        if(newDownload) {
            NewDownload(
                onSuccess = {item -> },
                onCancel = {
                    newDownload = false
                }
            )
        }

        DismissibleNavigationDrawer(
            drawerContent = {
                DismissibleDrawerSheet {
                    Box(
                        modifier = Modifier
                            .height(500.dp)
                            .width(360.dp),
                    )
                }
            }
        ) {
            Scaffold (
                floatingActionButton = {
                    Button(
                        onClick = {
                            newDownload = true
                        }
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

                        ChaseRichToolTip(
                            tooltip = {
                                RichTooltip { Text("Browser Extensions") }
                            }
                        ) {
                            TextButton(
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier.height(36.dp).width(36.dp),
                                shape = RoundedCornerShape(8.dp),
                                onClick = {
                                    coroutineScope.launch {
                                        extensionDrawerState.open()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Face,
                                    contentDescription = "Browser Extensions"
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

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
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(0)
                                    }
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
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(1)
                                    }
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
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(2)
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Settings,
                                    contentDescription = "Settings"
                                )
                            }
                        }
                    }

                    HorizontalPager(
                        modifier = Modifier
                            .weight(1f)
                            .padding(paddingValues),
                        state = pagerState
                    ) { page ->

                        when(page) {
                            0 -> Downloads()
                            1 -> NetworkMonitor()
                            2 -> Settings()
                        }
                    }
                }
            }
        }
    }
}

//AnimatedVisibility(showContent) {
//    val greeting = remember { Greeting().greet() }
//    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//        Image(painterResource(Res.drawable.compose_multiplatform), null)
//        Text("Compose: $greeting")
//    }
//}