package org.slade.chase.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.slade.chase.repositories.DownloadsRepository

fun DownloadsViewModel.Companion.provideFactory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DownloadsViewModel(
            downloadsRepository = DownloadsRepository()
        ) as T
    }
}