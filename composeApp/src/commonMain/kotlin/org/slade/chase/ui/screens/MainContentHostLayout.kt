package org.slade.chase.ui.screens

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator

@Composable
expect fun MainContentHostLayout(
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    navigator: Navigator,
    content: @Composable BoxScope.() -> Unit
)