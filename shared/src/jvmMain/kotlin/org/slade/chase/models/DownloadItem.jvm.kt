package org.slade.chase.models

import org.slade.chase.RANGE_THRESH_HOLD
import org.slade.chase.Settings
import org.slade.chase.ranges
import java.io.Serializable
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID

actual class DownloadItem private actual constructor(url: String): IDownloadItem, Serializable {
    override val id: String = UUID.randomUUID().toString()

    override val source: String = url

    override var parts: List<DownloadPart> = emptyList()

    override var transferEncoding: TransferEncoding = TransferEncoding.Raw

    init {

        val uri = URL(source)

        // Open the url connection
        val httpURLConnection: HttpURLConnection = uri.openConnection() as HttpURLConnection

        httpURLConnection.requestMethod = "OPTIONS"

        val responseCode = httpURLConnection.responseCode

        httpURLConnection.inputStream.close()

        if(responseCode == HttpURLConnection.HTTP_OK) {

            val acceptRanges = httpURLConnection.headerFields["Accept-Ranges"]?.let { if(it.isNotEmpty()) it[0] else null } ?: ""
            val contentLength = httpURLConnection.contentLengthLong

            if(acceptRanges == "bytes" && contentLength > RANGE_THRESH_HOLD) {

                transferEncoding = TransferEncoding.Range

                parts = contentLength
                    .ranges(Settings.systemThreadCount)
                    .mapIndexed { index, (offset, end) ->
                        DownloadPart(
                            index = index,
                            offset = offset,
                            end = end
                        )
                    }

            } else {

                transferEncoding = TransferEncoding.Raw

                parts = listOf(
                    DownloadPart(index = 0, offset = 0L, end = contentLength - 1)
                )
            }
        }
    }

    actual val contentLength: Long by lazy {
        try {
            parts.last().end + 1
        } catch (e: NoSuchElementException) {
            0L
        }
    }

    actual companion object {
        actual fun init(url: String): DownloadItem {
            return DownloadItem(url)
        }
    }
}