package org.slade.chase

import org.slade.chase.models.DownloadItem
import org.slade.chase.models.DownloadState
import java.nio.file.Files
import java.nio.file.Path

@Suppress("DefaultLocale")
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

fun Path.ensureDirectoryCreated(): Path {
    if(Files.notExists(this) && !Files.isDirectory(this)) {
        Files.createDirectories(this)
    }
    return this
}