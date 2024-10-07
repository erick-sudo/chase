package org.slade.chase

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.slade.chase.ui.theme.ChaseTheme

@Composable
fun VoyagerApp() {

    ChaseTheme{
        Navigator(PostsScreen()) { navigator ->
            Scaffold(
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {}
                    ) {
                        Text("Test")
                    }
                },
                bottomBar = {
                    BottomAppBar {
                        listOf(
                            "Posts" to PostsScreen(),
                            "Create" to CreatePostScreen(),
                            "Notifications" to NotificationScreen()
                        ).map { (title, screen) ->
                            NavigationBarItem(
                                icon = {
                                    Icon(
                                        imageVector =  Icons.Outlined.Face,
                                        contentDescription = null
                                    )
                                },
                                onClick = {
                                    navigator.push(screen)
                                },
                                label = {
                                    Text(text = title)
                                },
                                selected = false
                            )
                        }
                    }
                }
            ) {
                CurrentScreen()
            }
        }
    }
}

class PostsScreen: Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow

        Column(
            modifier = Modifier.fillMaxSize()
                .border(width = 2.dp, color = Color.Red)
        ) {
            Text("Posts Screen")

            Spacer(modifier = Modifier.height(8.dp))

            (1..5).map {
                OutlinedCard(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            navigator.push(PostDetail("$it"))
                        },
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(200.dp)
                    ) {
                        Text("$it")
                    }
                }
            }
        }
    }
}

class PostDetail(
    val postId: String
): Screen {
    @Composable
    override fun Content() {
        Column {
            Text("Post Detail\n#$postId")
        }
    }
}

class CreatePostScreen: Screen {
    @Composable
    override fun Content() {
        Column {
            Text("Create Post Screen")
        }
    }
}

class NotificationScreen: Screen {
    @Composable
    override fun Content() {
        Column {
            Text("Notification Screen")
        }
    }
}