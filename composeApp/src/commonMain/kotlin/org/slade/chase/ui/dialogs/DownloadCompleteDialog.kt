package org.slade.chase.ui.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun DownloadCompleteDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    onCloseRequest: () -> Unit
)