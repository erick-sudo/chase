package org.slade.chase.repositories

import kotlinx.coroutines.flow.MutableStateFlow
import org.slade.chase.models.BytesReadCarrier
import org.slade.chase.models.DownloadItem

actual class DownloadsRepository : IDownloadsRepository {
    override suspend fun deserializeDownloadItem(id: String): DownloadItem? {
        return null
    }

    override suspend fun serialize(downloadItem: DownloadItem) {

    }

    override suspend fun serialize(downloadItems: List<DownloadItem>) {

    }

    override suspend fun deserializeDownloadItems(): List<DownloadItem> {
        return emptyList()
    }

    override suspend fun resolveMimeType(downloadItem: DownloadItem): String? {
        return null
    }

    override suspend fun downloadEntireFile(
        downloadItem: DownloadItem,
        bytesAssembledStateFlow: MutableStateFlow<BytesReadCarrier>
    ): Long {
        return 0L
    }

    override suspend fun downloadPart(
        downloadItem: DownloadItem,
        index: Int,
        bytesAssembledStateFlow: MutableStateFlow<BytesReadCarrier>
    ): Long {
        return 0L
    }

    override suspend fun downloadPartsParallel(
        downloadItem: DownloadItem,
        bytesAssembledStateFlows: List<MutableStateFlow<BytesReadCarrier>>
    ) {

    }

    override suspend fun assemble(
        downloadItem: DownloadItem,
        assembleBytesStateFlow: MutableStateFlow<BytesReadCarrier>
    ) {

    }
}