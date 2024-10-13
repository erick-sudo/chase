package org.slade.chase.repositories

import kotlinx.coroutines.flow.MutableStateFlow
import org.slade.chase.models.BytesReadCarrier
import org.slade.chase.models.DownloadItem

interface IDownloadsRepository {
    /**
     * Deserialize a single download item instance
     */
    suspend fun deserializeDownloadItem(
        id: String
    ): DownloadItem?

    /**
     * Serialize a download item
     */
    suspend fun serialize(
        downloadItem: DownloadItem
    )

    /**
     * Serialize download items
     */
    suspend fun serialize(
        downloadItems: List<DownloadItem>
    )

    /**
     * Deserialize download items
     */
    suspend fun deserializeDownloadItems(): List<DownloadItem>

    /**
     * Obtain mime type from the first 512 bytes of the file head.
     */
    suspend fun resolveMimeType(
        downloadItem: DownloadItem
    ): String?

    /**
     * Download the entire file
     */
    suspend fun downloadEntireFile(
        downloadItem: DownloadItem,
        bytesAssembledStateFlow: MutableStateFlow<BytesReadCarrier>
    ): Long

    /**
     * Retrieve a range of bytes from a remote host
     * and write to a destination path.
     * @return the number of written bytes
     */
    suspend fun downloadPart(
        downloadItem: DownloadItem,
        index: Int,
        bytesAssembledStateFlow: MutableStateFlow<BytesReadCarrier>
    ): Long

    suspend fun downloadPartsParallel(
        downloadItem: DownloadItem,
        bytesAssembledStateFlows: List<MutableStateFlow<BytesReadCarrier>>
    )

    /**
     * Read a download item from a state file,
     * determine mime type,
     * and save file to the configured download directory.
     */
    suspend fun assemble(
        downloadItem: DownloadItem,
        assembleBytesStateFlow: MutableStateFlow<BytesReadCarrier>
    )
}

expect class DownloadsRepository(): IDownloadsRepository