package org.slade.chase.ui.dialogs

import androidx.compose.runtime.Composable

@Composable
actual fun openFileDialog(
    title: String,
    allowMultiple: Boolean,
    mode: FileDialogMode,
    allowedExtensions: List<String>,
    onCloseRequest: () -> Unit,
    onResult: (Set<String>) -> Unit
) {

}