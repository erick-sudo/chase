package org.slade.chase.repositories

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slade.chase.MimeTypesMap
import org.slade.chase.STATE_FILE_NAME
import org.slade.chase.Settings
import org.slade.chase.ensureDirectoryCreated
import org.slade.chase.models.BytesReadCarrier
import org.slade.chase.models.DownloadItem
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

fun Path.fileName(): String {
    return  if(nameCount > 0) getName(nameCount - 1).toString() else ""
}

fun URL.fileName(): String {
    return Path.of(path).fileName()
}

actual class DownloadsRepository : IDownloadsRepository {
    override suspend fun deserializeDownloadItem(id: String): DownloadItem? = coroutineScope {
        deserializeDownloadItem(
            File(
                Settings.temporaryDirectory.toFile(),
                id
            )
        )
    }

    override suspend fun serialize(
        downloadItem: DownloadItem
    ) = coroutineScope {
        val rootDir = Path.of(Settings.temporaryDirectory.toString(), downloadItem.id).ensureDirectoryCreated()

        val downloadItemJsonString = Json.encodeToString(downloadItem)

        Files.newOutputStream(Path.of(rootDir.toString(), STATE_FILE_NAME)).use { oStream ->
            oStream.write(downloadItemJsonString.toByteArray())
        }
    }

    override suspend fun serialize(downloadItems: List<DownloadItem>) = coroutineScope {
        downloadItems.map { downloadItem ->
            launch {
                serialize(downloadItem)
            }
        }.joinAll()
    }

    override suspend fun deserializeDownloadItems(): List<DownloadItem> = coroutineScope {
        try {
            val items: MutableList<DownloadItem> = mutableListOf()
            Files.newDirectoryStream(Settings.temporaryDirectory).use { dirstrm ->
                for (rootPath in dirstrm) {
                    val expectedStateFilePath = Path.of(rootPath.toString(), STATE_FILE_NAME)
                    if (Files.isDirectory(rootPath) && Files.exists(expectedStateFilePath)) {

                        // Only pick non null deserialization
                        deserializeDownloadItem(rootPath)?.also {
                            items += it
                        }
                    }
                }
            }
            items
        } catch (exc: IOException) {
            exc.printStackTrace()
            emptyList()
        }
    }

    override suspend fun resolveMimeType(
        downloadItem: DownloadItem
    ): String? = coroutineScope {
        try {
            FileInputStream(Path.of(Settings.temporaryDirectory.toString(), downloadItem.id, downloadItem.parts.getOrNull(0)?.id ?: "").toFile())
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

    override suspend fun downloadEntireFile(
        downloadItem: DownloadItem,
        bytesAssembledStateFlow: MutableStateFlow<BytesReadCarrier>
    ): Long = coroutineScope {
        val url = URL(downloadItem.source)

        // Open the url connection
        val httpURLConnection: HttpURLConnection = url.openConnection() as HttpURLConnection

        httpURLConnection.requestMethod = "GET"

        // Make actual connection to url
        httpURLConnection.connect()

        downloadItem.parts.getOrNull(0)?.let { part ->
            readWrite(
                httpURLConnection.inputStream,
                FileOutputStream(
                    Path.of(
                        Settings.temporaryDirectory.toString(),
                        downloadItem.id,
                        part.id
                    ).toFile()
                )
            ) { bytesWritten ->
                bytesAssembledStateFlow.value = BytesReadCarrier(
                    part.id,
                    part.index,
                    bytesWritten
                )
            }
        } ?: 0L
    }

    override suspend fun downloadPart(
        downloadItem: DownloadItem,
        index: Int,
        bytesAssembledStateFlow: MutableStateFlow<BytesReadCarrier>
    ): Long = coroutineScope {
        val url = URL(downloadItem.source)

        downloadItem.parts.getOrNull(index)?.let { part ->
            // Open the url connection
            val httpURLConnection: HttpURLConnection = url.openConnection() as HttpURLConnection

            httpURLConnection.requestMethod = "GET"
            httpURLConnection.setRequestProperty("Range", "bytes=${part.offset}-${part.end}")

            // Make actual connection to url
            httpURLConnection.connect()

            readWrite(
                httpURLConnection.inputStream,
                FileOutputStream(
                    Path.of(
                        Settings.temporaryDirectory.toString(),
                        downloadItem.id,
                        part.id
                    ).toFile()
                ),
            ) { bytesWritten ->
                bytesAssembledStateFlow.value = BytesReadCarrier(
                    part.id,
                    part.index,
                    bytesWritten
                )
            }
        }?: 0
    }

    override suspend fun downloadPartsParallel(
        downloadItem: DownloadItem,
        bytesAssembledStateFlows: List<MutableStateFlow<BytesReadCarrier>>
    ) = coroutineScope {
        List(downloadItem.parts.size) { index ->
            launch {
                downloadPart(
                    downloadItem,
                    index,
                    bytesAssembledStateFlows[index]
                )
            }
        }.joinAll()
    }

    override suspend fun assemble(
        downloadItem: DownloadItem,
        assembleBytesStateFlow: MutableStateFlow<BytesReadCarrier>
    ) = coroutineScope {
        val saveTo = savingTo(URL(downloadItem.source).fileName())

        val outputStream = FileOutputStream(saveTo.toFile())

        val magicBytes = mutableListOf<Byte>()

        var bytesLeft = 512;

        downloadItem.parts.forEach { part ->
            val partFile = Path.of(Settings.temporaryDirectory.toString(), downloadItem.id, part.id)
            if (Files.exists(partFile) && partFile.toFile().isFile) {
                FileInputStream(partFile.toFile()).use { iStream ->
                    val buffer = ByteArray(1024)
                    var bytesRead: Int

                    var bytesAssembled = 0L

                    while ((iStream.read(buffer).also { bytesRead = it }) != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                        bytesAssembled += bytesRead

                        // Emit number of bytes assembled
                        assembleBytesStateFlow.also {
                            it.value = BytesReadCarrier(part.id, part.index, bytesAssembled)
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

    private fun savingTo(fileName: String): Path {
        var path: Path
        var count = 0;
        do {
            path = Path.of(Settings.downloadsDirectory.toString(), "${(if(count > 0) "copy_${count}_" else "")}$fileName")
            count++
        } while (Files.exists(path))
        return Path.of(path.toString().substringBeforeLast("."))
    }

    private suspend fun deserializeDownloadItem(root: Path): DownloadItem? = coroutineScope {
        deserializeDownloadItem(root.toFile())
    }

    private suspend fun deserializeDownloadItem(root: File): DownloadItem? = coroutineScope {

        val stateFilePath = Path.of(root.toString(), STATE_FILE_NAME)

        if(Files.notExists(stateFilePath)) {
            return@coroutineScope null
        }

        BufferedReader(
            InputStreamReader(
                Files.newInputStream(stateFilePath)
            )
        ).use { bufferedReader ->
            val jsonString = bufferedReader.readLines().joinToString("")
            Json.decodeFromString<DownloadItem>(jsonString)
        }
    }

    companion object {
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
            onBytesWritten: (Long) -> Unit
        ): Long = coroutineScope {
            var successfulBytes: Long = 0
            inputStream.use { iStream ->
                val buffer = ByteArray(1024)
                var bytesRead: Int
                while ((iStream.read(buffer).also { bytesRead = it }) != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                    successfulBytes+=bytesRead
                    onBytesWritten(successfulBytes)
                }
                outputStream.flush()
                bytesRead
            }

            successfulBytes;
        }
    }
}