package org.slade.chase.models

import org.slade.chase.RANGE_THRESH_HOLD
import org.slade.chase.Settings
import org.slade.chase.ranges
import java.net.HttpURLConnection
import java.net.URL

fun DownloadItem.Companion.init(url: String): DownloadItem = init().apply {
    source = url
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

            parts.clear()
            parts.addAll(
                contentLength
                    .ranges(Settings.systemThreadCount)
                    .mapIndexed { index, (offset, end) ->
                        DownloadPart(
                            index = index,
                            offset = offset,
                            end = end
                        )
                    }
            )

        } else {

            transferEncoding = TransferEncoding.Raw

            parts.clear()
            parts.add(
                DownloadPart(index = 0, offset = 0L, end = contentLength - 1)
            )
        }
    }
}