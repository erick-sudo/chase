package org.slade.chase.tasks

import kotlinx.coroutines.flow.MutableStateFlow
import org.slade.chase.models.DownloadItem

/**
 * Deserialize a single download item instance
 */
expect suspend fun deserializeDownloadItem(id: String): DownloadItem

/**
 * Serialize a download item
 */
expect suspend fun DownloadItem.serialize()

/**
 * Deserialize download items
 */
expect suspend fun deserializeDownloadItems(): List<DownloadItem>

/**
 * Obtain mime type from the first 512 bytes of the file head.
 */
expect suspend fun DownloadItem.resolveMimeType(): String?

/**
 * Download the entire file
 */
expect suspend fun DownloadItem.downloadEntireFile(): Long

/**
 * Retrieve a range of bytes from a remote host
 * and write to a destination path.
 * @return the number of written bytes
 */
expect suspend fun DownloadItem.downloadPart(index: Int): Long

expect suspend fun DownloadItem.downloadPartsParallel()

/**
 * Read a download item from a state file,
 * determine mime type,
 * and save file to the configured download directory.
 */
expect suspend fun DownloadItem.assemble(assembleBytesStateFlow: MutableStateFlow<Long>)