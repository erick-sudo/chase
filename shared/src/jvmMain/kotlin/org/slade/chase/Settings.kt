package org.slade.chase

import java.nio.file.Files
import java.nio.file.Path

object Settings {
    val temporaryDirectory: Path by lazy {
        val  path = Path.of(System.getProperty("user.home"), ".chase", "tmp")
        if(!Files.exists(path) && !Files.isDirectory(path)) {
            Files.createDirectories(path)
        }
        path
    }

    val systemThreadCount: Int by lazy {
        4
    }

    val downloadsDirectory: Path by lazy {
        val path = Path.of(System.getProperty("user.home"), "Chase")
        if(!Files.exists(path) || !Files.isDirectory(path)) {
            Files.createDirectories(path)
        }
        path
    }
}