package org.slade.chase.models

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface IDownloadItem {
    val id: String
    var source: String
    val parts: MutableList<DownloadPart>
    var transferEncoding: TransferEncoding
    val contentLength: Long
}

@Serializable
class DownloadItem private constructor() : IDownloadItem {

    @OptIn(ExperimentalUuidApi::class)
    override val id: String = Uuid.random().toString()

    override var source: String = ""

    override val parts: MutableList<DownloadPart> = mutableListOf()

    override var transferEncoding: TransferEncoding = TransferEncoding.Raw

    override val contentLength: Long by lazy {
        try {
            parts.last().end + 1
        } catch (e: NoSuchElementException) {
            0L
        }
    }

    companion object {
        fun init(): DownloadItem = DownloadItem()
    }
}