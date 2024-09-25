package org.slade.chase

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.slade.chase.ui.progress.SpeedoMeter
import org.slade.chase.ui.theme.ChaseTheme

@Composable
@Preview
fun App() {

    ChaseTheme {

        SpeedoMeter(
            modifier = Modifier
                .size(70.dp),
            value = 40f
        )

//        val coroutineScope = rememberCoroutineScope()
//
//        var downloadItems by remember {
//            mutableStateOf<List<Pair<DownloadItem, List<MutableStateFlow<BytesReadCarrier>>>>>(emptyList())
//        }

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

//        Scaffold(
//            floatingActionButton = {
//                FloatingActionButton(
//                    onClick = { }
//                ) {
//                    Text("New")
//                }
//            }
//        ) { paddingValues ->
//
//            LazyColumn(
//                modifier = Modifier
//                    .padding(paddingValues)
//                    .fillMaxWidth(),
//            ) {
//                items(downloadItems) { (downloadItem, bytesReadStateFlows) ->
//                    DownloadListItem(
//                        downloadItem,
//                        bytesReadPartFlows = bytesReadStateFlows
//                    )
//                }
//
//                item {
//                    Button(
//                        onClick = {
//                            coroutineScope.launch {
//                                downloadItems = deserializeDownloadItems().map { downloadItem ->
//                                    downloadItem to downloadItem.parts.map { MutableStateFlow(BytesReadCarrier(it.id, it.index, it.retrieved)) }
//                                }
//                            }
//                        }
//                    ) {
//                        Text("Deserialize Items")
//                    }
//                }
//            }
//        }
    }
}

//AnimatedVisibility(showContent) {
//    val greeting = remember { Greeting().greet() }
//    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//        Image(painterResource(Res.drawable.compose_multiplatform), null)
//        Text("Compose: $greeting")
//    }
//}