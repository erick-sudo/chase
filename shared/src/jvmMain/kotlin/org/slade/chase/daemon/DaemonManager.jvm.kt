package org.slade.chase.daemon

import kotlinx.coroutines.channels.Channel
import org.slade.chase.Settings
import java.nio.file.FileSystems
import java.nio.file.StandardWatchEventKinds
import java.nio.file.WatchKey
import kotlinx.serialization.Serializable

fun main() {
    val sharedDirectoryPath = Settings.sharedDirectory

    val watchService = FileSystems.getDefault().newWatchService()

    sharedDirectoryPath.register(
        watchService,
        StandardWatchEventKinds.ENTRY_CREATE
    )

    val uiChannel: Channel<UIChaseInstruction> = Channel()

    var key: WatchKey
    while(watchService.take().also { key = it } != null) {
        for (event in key.pollEvents()) {
            println(event.kind().name())
        }
        key.reset()
    }
}