package org.slade.chase.ui.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.AwtWindow
import java.awt.FileDialog
import java.awt.Frame
import java.nio.file.Files

@Composable
actual fun openFileDialog(
    title: String,
    allowMultiple: Boolean,
    mode: FileDialogMode,
    allowedExtensions: List<String>,
    onCloseRequest: () -> Unit,
    onResult: (Set<String>) -> Unit
) = AwtWindow(
    create = {
        val parent: Frame? = null
        object : FileDialog(parent, title, LOAD) {
            override fun setVisible(b: Boolean) {
                super.setVisible(b)
                if (b) {
                    onResult(files.map { it.absolutePath }.toSet())
                    onCloseRequest()
                }
            }
        }.apply {
            isMultipleMode = allowMultiple

            file = allowedExtensions.joinToString(";") { "*$it" }

            setFilenameFilter { file, s ->
                if (mode == FileDialogMode.Directory) {
                    Files.isDirectory(file.toPath())
                } else {
                    if (allowedExtensions.isEmpty()) true else allowedExtensions.any { s.endsWith(it) }
                }
            }
        }
    },
    dispose = FileDialog::dispose
)