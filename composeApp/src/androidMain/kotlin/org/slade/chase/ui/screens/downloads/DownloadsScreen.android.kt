package org.slade.chase.ui.screens.downloads

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import org.slade.chase.viewmodels.DownloadsViewModel
import org.slade.chase.viewmodels.provideFactory

@Composable
actual fun provideDownloadsViewModel(): DownloadsViewModel {
    return viewModel(factory = DownloadsViewModel.provideFactory())
}