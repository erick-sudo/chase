package org.slade.chase.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import chase.composeapp.generated.resources.Res
import chase.composeapp.generated.resources.baseline_description_24
import chase.composeapp.generated.resources.outline_apps_24
import chase.composeapp.generated.resources.outline_audiotrack_24
import chase.composeapp.generated.resources.outline_filter_24
import chase.composeapp.generated.resources.outline_hangout_video_24
import chase.composeapp.generated.resources.outline_storage_24
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource
import org.slade.chase.FakeRepo
import org.slade.chase.models.BytesReadCarrier
import org.slade.chase.models.DownloadItem
import org.slade.chase.tasks.deserializeDownloadItems
import org.slade.chase.ui.DownloadListItem
import org.slade.chase.ui.theme.ChaseTheme

enum class DownloadCategory {
    All,
    Documents,
    Images,
    Videos,
    Audio,
    Programs
}


private val categoryIcons: List<DrawableResource> = listOf(
    Res.drawable.outline_storage_24,
    Res.drawable.baseline_description_24,
    Res.drawable.outline_filter_24,
    Res.drawable.outline_hangout_video_24,
    Res.drawable.outline_audiotrack_24,
    Res.drawable.outline_apps_24
)


@Composable
private fun All() {
    Column {
        Text("All")
    }
}

@Composable
private fun Documents() {
    Column {
        Text("Documents")
    }
}

@Composable
private fun Images() {
    Column {
        Text("Images")
    }
}

@Composable
private fun Videos() {
    Column {
        Text("Videos")
    }
}

@Composable
private fun Audio() {
    Column {
        Text("Audio")
    }
}

@Composable
private fun Programs() {
    Column {
        Text("Programs")
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Downloads() {

    val coroutineScope = rememberCoroutineScope()

    val downloadsHorizontalPagerState = rememberPagerState(initialPage = 1) {
        8
    }

    Column {
        Row(
            modifier = Modifier
                .padding(top = 8.dp, end = 8.dp, start = 8.dp, bottom = 4.dp)
                .horizontalScroll(rememberScrollState())
        ) {

            listOf(
                "Active Downloads",
                "Download History",
                *DownloadCategory.entries.map { it.name }.toTypedArray()
            ).mapIndexed { index, tab ->
                ElevatedButton(
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = if(index == downloadsHorizontalPagerState.currentPage) MaterialTheme.colorScheme.primaryContainer else Color.Unspecified,
                        contentColor = if(index == downloadsHorizontalPagerState.currentPage) MaterialTheme.colorScheme.onPrimaryContainer else Color.Unspecified
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 2.dp),
                    onClick = {
                        coroutineScope.launch {
                            downloadsHorizontalPagerState.animateScrollToPage(index)
                        }
                    }
                ) {
                    Text(
                        text = tab,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))
            }
        }

        HorizontalPager(
            state = downloadsHorizontalPagerState
        ) { page ->

            when(page) {
                0 -> ActiveDownloads()
                1 -> DownloadHistory(
                    onCategoryChange = { category ->
                        coroutineScope.launch {
                            downloadsHorizontalPagerState.animateScrollToPage(
                                2 + category.ordinal
                            )
                        }
                    }
                )
                2 -> All()
                3 -> Documents()
                4 -> Images()
                5 -> Videos()
                6 -> Audio()
                else -> Programs()
            }
        }
    }
}

@Composable
fun ActiveDownloads() {
    val coroutineScope = rememberCoroutineScope()

    var downloadItems by remember {
        mutableStateOf<List<Pair<DownloadItem, List<MutableStateFlow<BytesReadCarrier>>>>>(emptyList())
    }

    LaunchedEffect("DeserializeDownloadItems") {
        coroutineScope.launch {
            downloadItems = deserializeDownloadItems().map { downloadItem ->
                downloadItem to downloadItem.parts.map { MutableStateFlow(BytesReadCarrier(it.id, it.index, it.retrieved)) }
            }
//            val items = mutableListOf<Pair<DownloadItem, List<MutableStateFlow<BytesReadCarrier>>>>()
//            FakeRepo.downloadItems.map { url ->
//                launch {
//                    DownloadItem.init(url).also { downloadItem ->
//                        items += (downloadItem to downloadItem.parts.map { MutableStateFlow(BytesReadCarrier(it.id, it.index, it.retrieved)) })
//                    }
//                }
//            }.joinAll()
//            downloadItems = items
            //downloadItems.map { it.first }.serialize()
        }
    }

    LazyColumn(
        modifier = Modifier
    ) {
        itemsIndexed(downloadItems) { index, (downloadItem, bytesReadStateFlows) ->
            DownloadListItem(
                modifier = Modifier
                    .padding(8.dp),
                downloadItem = downloadItem,
                bytesReadPartFlows = bytesReadStateFlows
            )

            if(index < downloadItems.size - 1) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .border(
                            width = 1.dp,
                            brush = Brush.linearGradient(
                                colors = ChaseTheme.borderGradientColors.map { it.copy(alpha = 0.4f) }
                            ),
                            shape = RectangleShape
                        )
                )
            }
        }
    }
}

@Composable
fun DownloadHistory(
    onCategoryChange: (category: DownloadCategory) -> Unit
) {

    val coroutineScope = rememberCoroutineScope()

    val recentsLazyListState = rememberLazyListState()

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text(
                text = "Recents"
            )

            Spacer(modifier = Modifier.weight(1f))

            TextButton(
                onClick = {
                    onCategoryChange(DownloadCategory.All)
                }
            ) {
                Text("See All")
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 8.dp),
            ) {
                (0..10).map {
                    Card(
                        modifier = Modifier
                            .width(200.dp)
                            .height(240.dp)
                            .padding(4.dp)
                    ) {

                    }
                }
            }
        }

        Text(
            text = "Collections",
            modifier = Modifier
                .padding(8.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(240.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(DownloadCategory.entries.zip(categoryIcons)) { (category, icon) ->
                ElevatedButton(
                    shape = RoundedCornerShape(10.dp),
                    onClick = {
                        onCategoryChange(category)
                    },
                    modifier = Modifier
                        .padding(4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .height(96.dp)
                                .width(72.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(36.dp),
                                imageVector = vectorResource(icon),
                                contentDescription = category.name
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Column(
                            modifier = Modifier
                                .wrapContentHeight()
                        ) {
                            Text(text = category.name)

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(text = "0.3 GB")
                        }
                    }
                }
            }
        }
    }
}