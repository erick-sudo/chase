package org.slade.chase

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.slade.chase.ui.screens.MainContentHostLayout
import org.slade.chase.ui.screens.downloads.DownloadsScreen
import org.slade.chase.ui.theme.ChaseTheme

@Composable
@Preview
fun App() {

    val drawerState = rememberDrawerState(DrawerValue.Closed)

    ChaseTheme {
        Navigator(
            DownloadsScreen()
        ) { navigator ->
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet(
                        drawerShape = RectangleShape
                    ) {
                        Column(Modifier.verticalScroll(rememberScrollState())) {

                        }
                    }
                }
            ) {
                MainContentHostLayout(
                    modifier = Modifier,
                    drawerState = drawerState,
                    navigator = navigator
                ) {
                    CurrentScreen()
                }
            }
        }
    }
}