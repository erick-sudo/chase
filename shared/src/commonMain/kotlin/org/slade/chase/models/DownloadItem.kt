package org.slade.chase.models

interface IDownloadItem {
    val id: String
    val source: String
    var parts: List<DownloadPart>
    var transferEncoding: TransferEncoding
}

expect class DownloadItem private constructor (url: String): IDownloadItem {
    val contentLength: Long

    companion object {
        fun init(url: String): DownloadItem
    }
}