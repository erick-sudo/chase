package org.slade.chase.ui.screens.downloads

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import chase.composeapp.generated.resources.Res
import chase.composeapp.generated.resources.baseline_description_24
import chase.composeapp.generated.resources.outline_apps_24
import chase.composeapp.generated.resources.outline_audiotrack_24
import chase.composeapp.generated.resources.outline_filter_24
import chase.composeapp.generated.resources.outline_hangout_video_24
import chase.composeapp.generated.resources.outline_storage_24
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource

enum class DownloadCategory {
    All,
    Documents,
    Images,
    Videos,
    Audio,
    Programs
}

val categoryScreens = listOf(
    DownloadCategory.All to All(),
    DownloadCategory.Documents to Documents(),
    DownloadCategory.Images to Images(),
    DownloadCategory.Videos to Videos(),
    DownloadCategory.Audio to Audio(),
    DownloadCategory.Programs to Programs()
)

val categoryIcons: List<DrawableResource> = listOf(
    Res.drawable.outline_storage_24,
    Res.drawable.baseline_description_24,
    Res.drawable.outline_filter_24,
    Res.drawable.outline_hangout_video_24,
    Res.drawable.outline_audiotrack_24,
    Res.drawable.outline_apps_24
)

class DownloadsScreen: Screen {

    @Composable
    override fun Content() {
        Navigator(
            ActiveDownloadsScreen()
        ) { navigator ->
            Downloads(
                navigator = navigator
            ) {
                CurrentScreen()
            }
        }
    }

    override val key: ScreenKey
        get() = "Downloads"

}

@Composable
private fun Downloads(
    navigator: Navigator,
    content: @Composable () -> Unit
) {

    val downloadScreens = remember {
        listOf(
            "Active Downloads" to ActiveDownloadsScreen(),
            "Download History" to DownloadHistoryScreen(),
            *categoryScreens.map { it.first.name to it.second }.toTypedArray()
        )
    }

    val coroutineScope = rememberCoroutineScope()

    Column {
        Row(
            modifier = Modifier
                .padding(top = 8.dp, end = 8.dp, start = 8.dp, bottom = 4.dp)
                .horizontalScroll(rememberScrollState())
        ) {

            downloadScreens.map { tab ->
                val (title, screen) = tab

                val isActive = (navigator?.lastItem?.key ?: "") == screen.key

                ElevatedButton(
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = if(isActive) MaterialTheme.colorScheme.primaryContainer else Color.Unspecified,
                        contentColor = if(isActive) MaterialTheme.colorScheme.onPrimaryContainer else Color.Unspecified
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 2.dp),
                    onClick = {
                        coroutineScope.launch {
                            navigator?.push(screen)
                        }
                    }
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))
            }
        }

        content()
    }
}