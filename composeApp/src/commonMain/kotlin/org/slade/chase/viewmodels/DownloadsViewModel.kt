package org.slade.chase.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.slade.chase.FakeRepo
import org.slade.chase.models.BytesReadCarrier
import org.slade.chase.models.DownloadItem
import org.slade.chase.repositories.DownloadsRepository

data class DownloadsUIState(
    val downloadItems: List<DownloadItem> = emptyList()
)

class DownloadsViewModel(
    private val downloadsRepository: DownloadsRepository = DownloadsRepository()
): ViewModel() {

    private val _downloadsUiState: MutableStateFlow<DownloadsUIState> =  MutableStateFlow(DownloadsUIState())

    val downloadsUiState
        get() = _downloadsUiState.asStateFlow()

    init {
        viewModelScope.launch {
//            _downloadsUiState.value = _downloadsUiState.value.copy(
//                downloadItems = downloadsRepository.deserializeDownloadItems()
//            )

            val items = mutableListOf<DownloadItem>() // mutableListOf<Pair<DownloadItem, List<MutableStateFlow<BytesReadCarrier>>>>()
            FakeRepo.downloadItems.map { url ->
                launch {
                    DownloadItem.init(
                        url.replace("localhost", "10.0.2.2")
                    )?.also { downloadItem ->
                        items += downloadItem
//                        items += (downloadItem to downloadItem.parts.map { MutableStateFlow(
//                            BytesReadCarrier(it.id, it.index, it.retrieved)
//                        ) })
                    }
                }
            }.joinAll()
            _downloadsUiState.value = _downloadsUiState.value.copy(
                downloadItems = items
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
        return DownloadItem.init(FakeRepo.downloadItems.random())
        //return downloadsRepository.deserializeDownloadItem(id)
    }

    companion object
}