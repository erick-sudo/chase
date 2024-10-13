package org.slade.chase.models

import kotlinx.serialization.Serializable
import org.slade.chase.RANGE_THRESH_HOLD
import org.slade.chase.ranges
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
class DownloadItem private constructor(
    override var source: String = ""
) : IDownloadItem {

    @OptIn(ExperimentalUuidApi::class)
    override val id: String = Uuid.random().toString()

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
        suspend fun init(url: String): DownloadItem? {
            val downloadItem = DownloadItem(url)
//            val client = httpClient()
//
//            val response: HttpResponse = client.options(
//                downloadItem.source
//            )
//
//            if(response.status.value in 200..299) {
//
//                val acceptRanges = response.headers["Accept-Ranges"]?.let { if(it.isNotEmpty()) it[0] else null } ?: ""
//                val contentLength = response.contentLength() ?: 0L
//
//                if(acceptRanges == "bytes" && contentLength > RANGE_THRESH_HOLD) {
//
//                    downloadItem.transferEncoding = TransferEncoding.Range
//
//                    downloadItem.parts.clear()
//                    downloadItem.parts.addAll(
//                        contentLength
//                            .ranges(8) // Settings.systemThreadCount
//                            .mapIndexed { index, (offset, end) ->
//                                DownloadPart(
//                                    index = index,
//                                    offset = offset,
//                                    end = end
//                                )
//                            }
//                    )
//
//                } else {
//
//                    downloadItem.transferEncoding = TransferEncoding.Raw
//
//                    downloadItem.parts.clear()
//                    downloadItem.parts.add(
//                        DownloadPart(index = 0, offset = 0L, end = contentLength - 1)
//                    )
//                }
//
//                return downloadItem
//            }
//
//            return null

            val oneMb = 1024L * 1024L

            val acceptRanges = "bytes"
            val contentLength = (50L..1034L).random() * oneMb

            if(acceptRanges == "bytes" && contentLength > RANGE_THRESH_HOLD) {

                downloadItem.transferEncoding = TransferEncoding.Range

                downloadItem.parts.clear()
                downloadItem.parts.addAll(
                    contentLength
                        .ranges(8) // Settings.systemThreadCount
                        .mapIndexed { index, (offset, end) ->
                            DownloadPart(
                                index = index,
                                offset = offset,
                                end = end
                            )
                        }
                )

            } else {

                downloadItem.transferEncoding = TransferEncoding.Raw

                downloadItem.parts.clear()
                downloadItem.parts.add(
                    DownloadPart(index = 0, offset = 0L, end = contentLength - 1)
                )
            }

            return downloadItem
        }
    }
}