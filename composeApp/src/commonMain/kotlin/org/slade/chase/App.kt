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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
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
import androidx.compose.ui.graphics.RectangleShape
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

    val items =
        listOf(
            Icons.Default.AccountCircle,
            Icons.Default.ThumbUp,
            Icons.Default.DateRange,
            Icons.Default.Notifications,
            Icons.Default.Email,
            Icons.Default.Favorite,
            Icons.Default.Face,
            Icons.Default.ShoppingCart,
            Icons.Default.Warning,
            Icons.Default.Star,
            Icons.Default.Share,
            Icons.Default.Refresh,
            Icons.Default.Lock,
            Icons.Default.Phone,
            Icons.Default.Info,
            Icons.Default.Search,
        )

    ChaseTheme {

        if(newDownload) {
            NewDownload(
                onSuccess = {item -> },
                onCancel = {
                    newDownload = false
                }
            )
        }

        ModalNavigationDrawer(
            drawerState = extensionDrawerState,
            drawerContent = {
                ModalDrawerSheet(
                    drawerShape = RectangleShape
                ) {
                    Column(Modifier.verticalScroll(rememberScrollState())) {
                        Spacer(Modifier.height(12.dp))
                        items.forEach { item ->
                            NavigationDrawerItem(
                                shape = RectangleShape,
                                icon = { Icon(item, contentDescription = null) },
                                label = { Text(item.name.substringAfterLast(".")) },
                                selected = item == Icons.Default.Star,
                                onClick = {
                                    coroutineScope.launch { extensionDrawerState.close() }
                                },
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                        }
                    }
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