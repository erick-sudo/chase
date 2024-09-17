package org.slade.chase.tasks

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.slade.chase.MimeTypesMap
import org.slade.chase.STATE_FILE_NAME
import org.slade.chase.Settings
import org.slade.chase.fileName
import org.slade.chase.models.DownloadItem
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

actual suspend fun deserializeDownloadItem(id: String): DownloadItem {
    return deserializeDownloadItem(File(Settings.temporaryDirectory.toFile(), id))
}

private suspend fun deserializeDownloadItem(root: Path): DownloadItem {
    return deserializeDownloadItem(root.toFile())
}

private suspend fun deserializeDownloadItem(root: File): DownloadItem = coroutineScope {
    ObjectInputStream(FileInputStream(File(root, STATE_FILE_NAME))).use { iStream ->
        iStream.readObject() as DownloadItem
    }
}

actual suspend fun DownloadItem.serialize() = coroutineScope {
    ObjectOutputStream(Files.newOutputStream(Path.of(Settings.temporaryDirectory.toString(), this@serialize.id, STATE_FILE_NAME))).use { oStream ->
        oStream.writeObject(this@serialize)
    }
}

actual suspend fun deserializeDownloadItems(): List<DownloadItem> = coroutineScope {
    try {
        val items: MutableList<DownloadItem> = mutableListOf()
        Files.newDirectoryStream(Settings.temporaryDirectory).use { dirstrm ->
            for (path in dirstrm) {
                if (Files.isDirectory(path)) {
                    items += deserializeDownloadItem(path.toFile())
                }
            }
        }
        items
    } catch (exc: IOException) {
        emptyList()
    }
}

actual suspend fun DownloadItem.resolveMimeType(): String? = coroutineScope {
    try {
        FileInputStream(Path.of(Settings.temporaryDirectory.toString(), this@resolveMimeType.id, parts.getOrNull(0)?.id ?: "").toFile())
            .use { iStream ->
                // Read first 512 bytes
                val magicBytes = iStream.readNBytes(512)
                resolveMimeType(magicBytes)
            }
    } catch (_: Exception) {
        null
    }
}

@OptIn(ExperimentalStdlibApi::class)
private fun resolveMimeType(bytes: ByteArray) = MimeTypesMap.entries.find { bytes.toHexString().startsWith(it.key) }?.value

/**
 * Read bytes from the provided input stream and
 * write bytes to the specified output stream.
 * The output stream is flushed when the operation is complete.
 * @param inputStream source of bytes.
 * @param outputStream destination of retrieved bytes.
 * @return the total number of successfully written bytes.
 */
suspend fun readWrite(
    inputStream: InputStream,
    outputStream: OutputStream,
    bytesReadStateFlow: MutableStateFlow<Long>? = null
): Long = coroutineScope {
    var successfulBytes: Long = 0
    inputStream.use { iStream ->
        val buffer = ByteArray(1024)
        var bytesRead: Int
        while ((iStream.read(buffer).also { bytesRead = it }) != -1) {
            outputStream.write(buffer, 0, bytesRead)
            successfulBytes+=bytesRead
            bytesReadStateFlow?.also {
                it.value = successfulBytes
            }
        }
        outputStream.flush()
        bytesRead
    }

    successfulBytes;
}

/**
 * Download the entire file
 */
actual suspend fun DownloadItem.downloadEntireFile(): Long = coroutineScope {

    val url = URL(this@downloadEntireFile.source)

    // Open the url connection
    val httpURLConnection: HttpURLConnection = url.openConnection() as HttpURLConnection

    httpURLConnection.requestMethod = "GET"

    // Make actual connection to url
    httpURLConnection.connect()

    parts.getOrNull(0)?.let { part ->
        readWrite(
            httpURLConnection.inputStream,
            FileOutputStream(Path.of(Settings.temporaryDirectory.toString(), this@downloadEntireFile.id, part.id).toFile())
        )
    } ?: 0L
}

/**
 * Retrieve a range of bytes from a remote host
 * and write to a destination path.
 * @return the number of written bytes
 */
actual suspend fun DownloadItem.downloadPart(
    index: Int
): Long = coroutineScope {

    val url = URL(this@downloadPart.source)

    this@downloadPart.parts.getOrNull(index)?.let { part ->
        // Open the url connection
        val httpURLConnection: HttpURLConnection = url.openConnection() as HttpURLConnection

        httpURLConnection.requestMethod = "GET"
        httpURLConnection.setRequestProperty("Range", "bytes=${part.offset}-${part.end}")

        // Make actual connection to url
        httpURLConnection.connect()

        readWrite(
            httpURLConnection.inputStream,
            FileOutputStream(Path.of(Settings.temporaryDirectory.toString(), this@downloadPart.id, part.id).toFile())
        )
    }?: 0
}

actual suspend fun DownloadItem.downloadPartsParallel() = coroutineScope {
    List(parts.size) { index ->
        launch {
            this@downloadPartsParallel.downloadPart(index)
        }
    }.joinAll()
}

fun savingTo(fileName: String): Path {
    var path: Path
    var count = 0;
    do {
        path = Path.of(Settings.downloadsDirectory.toString(), "${(if(count > 0) "copy_${count}_" else "")}$fileName")
        count++
    } while (Files.exists(path))
    return Path.of(path.toString().substringBeforeLast("."))
}

/**
 * Read a download item from a state file,
 * determine mime type,
 * and save file to the configured download directory.
 */
actual suspend fun DownloadItem.assemble(
    assembleBytesStateFlow: MutableStateFlow<Long>? = null
) = coroutineScope {

    val saveTo = savingTo(URL(this@assemble.source).fileName())

    val outputStream = FileOutputStream(saveTo.toFile())

    val magicBytes = mutableListOf<Byte>()

    var bytesLeft = 512;

    this@assemble.parts.forEach { part ->
        val partFile = Path.of(Settings.temporaryDirectory.toString(), this@assemble.id, part.id)
        if (Files.exists(partFile) && partFile.toFile().isFile) {
            FileInputStream(partFile.toFile()).use { iStream ->
                val buffer = ByteArray(1024)
                var bytesRead: Int

                var bytesAssembled = 0L

                while ((iStream.read(buffer).also { bytesRead = it }) != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                    bytesAssembled += bytesRead

                    // Emit number of bytes assembled
                    assembleBytesStateFlow?.also {
                        it.value = bytesAssembled
                    }

                    // Attempt assembling the file's magic bytes
                    if(part.index == 0 && bytesLeft > 0) {
                        val bytesWithinRange = when {
                            (bytesRead > bytesLeft) -> bytesRead - bytesLeft
                            bytesLeft > bytesRead -> bytesRead
                            else -> bytesLeft
                        }
                        bytesLeft -= if(bytesRead > bytesLeft) bytesLeft else bytesRead
                        magicBytes += (0 until bytesWithinRange).map { buffer[it] }
                    }
                }
            }
        }
    }
    outputStream.flush()

    resolveMimeType(magicBytes.toByteArray())?.let {
        Files.move(saveTo, Path.of("$saveTo.$it"), StandardCopyOption.REPLACE_EXISTING)
    }

    // Files.delete(temporaryDestinationDirectory.toPath())

    outputStream.close()
}