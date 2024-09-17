package org.slade.chase

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DrawerValue
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.slade.chase.ui.DownloadItem

@Composable
@Preview
fun App() {
    MaterialTheme {

        val coroutineScope = rememberCoroutineScope()

        val drawerState = rememberDrawerState(DrawerValue.Closed)

        val scaffoldState = rememberScaffoldState(
            drawerState = drawerState
        )

        Scaffold(
            scaffoldState = scaffoldState,
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxWidth(),
            ) {
                DownloadItem()
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