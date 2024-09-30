package org.slade.chase.ui.dialogs

import androidx.compose.runtime.Composable

@Composable
expect fun openFileDialog(
    title: String = "Choose File",
    allowMultiple: Boolean = false,
    mode: FileDialogMode = FileDialogMode.File,
    allowedExtensions: List<String> = emptyList(),
    onCloseRequest: () -> Unit,
    onResult: (Set<String>) -> Unit
)

enum class FileDialogMode {
    File,
    Directory
}