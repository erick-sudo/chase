package org.slade.chase

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.slade.chase.ui.screens.Downloads
import org.slade.chase.ui.screens.NetworkMonitor
import org.slade.chase.ui.screens.Settings
import org.slade.chase.ui.theme.ChaseTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun App() {

    val pagerState = rememberPagerState(0) {
        3
    }

    ChaseTheme {
        Scaffold (
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { }
                ) {
                    Text("New")
                }
            }
        ) { paddingValues ->

            HorizontalPager(
                modifier = Modifier
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

//AnimatedVisibility(showContent) {
//    val greeting = remember { Greeting().greet() }
//    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//        Image(painterResource(Res.drawable.compose_multiplatform), null)
//        Text("Compose: $greeting")
//    }
//}