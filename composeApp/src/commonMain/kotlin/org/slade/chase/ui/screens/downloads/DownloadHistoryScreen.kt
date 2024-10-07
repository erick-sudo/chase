package org.slade.chase.ui.screens.downloads

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import org.jetbrains.compose.resources.vectorResource

class DownloadHistoryScreen: Screen {

    override val key: ScreenKey
        get() = "DownloadHistory"

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

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
                        navigator?.push(All())
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
                items(categoryScreens.zip(categoryIcons)) { (categoryScreen, icon) ->

                    val (category, screen) = categoryScreen

                    ElevatedButton(
                        shape = RoundedCornerShape(10.dp),
                        onClick = {
                            navigator?.push(screen)
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
}