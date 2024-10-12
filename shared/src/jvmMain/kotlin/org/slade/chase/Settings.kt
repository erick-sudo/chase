package org.slade.chase

import java.nio.file.Files
import java.nio.file.Path

object Settings {
    val temporaryDirectory: Path by lazy {
        Path.of(System.getProperty("user.home"), ".chase", "tmp")
            .ensureDirectoryCreated()
    }

    val sharedDirectory: Path by lazy {
        Path.of(System.getProperty("user.home"), ".chase", "daemon")
            .ensureDirectoryCreated()
    }

    val systemThreadCount: Int by lazy {
        8
    }

    val downloadsDirectory: Path by lazy {
        val path = Path.of(System.getProperty("user.home"), "Chase")
        if(!Files.exists(path) || !Files.isDirectory(path)) {
            Files.createDirectories(path)
        }
        path
    }
}