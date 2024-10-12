package org.slade.chase

import android.annotation.SuppressLint
import org.slade.chase.models.DownloadItem
import org.slade.chase.models.DownloadState

@SuppressLint("DefaultLocale")
actual fun Double.suffixByteSize(): String {
    var final = this + 0.0
    var i = 0
    while (final > 1024) {
        final /= 1024.0
        i++
    }

    return "${String.format("%.2f", final)} ${byteUnits[i]}"
}

actual fun DownloadItem.inferState(): DownloadState {
    TODO("Not yet implemented")
}