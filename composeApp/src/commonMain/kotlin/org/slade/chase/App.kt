package org.slade.chase

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.slade.chase.models.BytesReadCarrier
import org.slade.chase.models.DownloadItem
import org.slade.chase.tasks.deserializeDownloadItems
import org.slade.chase.ui.DownloadListItem
import org.slade.chase.ui.theme.ChaseTheme

@Composable
@Preview
fun App() {

    ChaseTheme {

        val coroutineScope = rememberCoroutineScope()

        var downloadItems by remember {
            mutableStateOf<List<Pair<DownloadItem, List<MutableStateFlow<BytesReadCarrier>>>>>(emptyList())
        }

//        LaunchedEffect(downloadItems) {
//            coroutineScope.launch {
//                downloadItems.getOrNull(2)?.let { (_, flw) ->
//                    var i = 0L
//                    while (true) {
//                        flw.value = flw.value.copy(index = (0..8).random(), numberOfBytes =  i)
//                        delay(10)
//                        i++
//                    }
//                }
//            }
//        }

//        LaunchedEffect("InitializeDownloadItems") {
//            coroutineScope.launch {
//                val items = mutableListOf<DownloadItem>()
//
//                FakeRepo.downloadItems.map { url ->
//                    launch {
//                        val item = DownloadItem(url)
//                        items += item
//                    }
//                }.joinAll()
//
//                downloadItems = items
//            }
//        }

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { }
                ) {
                    Text("New")
                }
            }
        ) { paddingValues ->

            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxWidth(),
            ) {
                items(downloadItems) { (downloadItem, bytesReadStateFlows) ->
                    DownloadListItem(
                        downloadItem,
                        bytesReadPartFlows = bytesReadStateFlows
                    )
                }

                item {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                downloadItems = deserializeDownloadItems().map { downloadItem ->
                                    downloadItem to downloadItem.parts.map { MutableStateFlow(BytesReadCarrier(it.id, it.index, it.retrieved)) }
                                }
                            }
                        }
                    ) {
                        Text("Deserialize Items")
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