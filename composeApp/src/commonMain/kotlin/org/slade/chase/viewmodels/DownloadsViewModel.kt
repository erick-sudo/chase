package org.slade.chase.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.slade.chase.models.DownloadItem
import org.slade.chase.repositories.DownloadsRepository

data class DownloadsUIState(
    val downloadItems: List<DownloadItem> = emptyList()
)

class DownloadsViewModel(
    private val downloadsRepository: DownloadsRepository
): ViewModel() {

    private val _downloadsUiState: MutableStateFlow<DownloadsUIState> =  MutableStateFlow(DownloadsUIState())

    val downloadsUiState
        get() = _downloadsUiState.asStateFlow()

    init {
        viewModelScope.launch {
            _downloadsUiState.value = _downloadsUiState.value.copy(
                downloadItems = downloadsRepository.deserializeDownloadItems()
            )
        }
    }

    fun serialize(downloadItem: DownloadItem) {
        viewModelScope.launch {
            downloadsRepository.serialize(downloadItem)
        }
    }

    fun serialize(downloadItems: List<DownloadItem>) {
        viewModelScope.launch {
            downloadsRepository.serialize(downloadItems)
        }
    }

    suspend fun deserializeDownloadItem(id: String): DownloadItem? {
        return downloadsRepository.deserializeDownloadItem(id)
    }

    companion object
}