package org.slade.chase.ui.screens.downloads

import androidx.compose.runtime.Composable
import org.slade.chase.viewmodels.DownloadsViewModel

@Composable
actual fun provideDownloadsViewModel(): DownloadsViewModel {
    return DownloadsViewModel()
}