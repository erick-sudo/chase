package org.slade.chase.tasks

import java.io.File

actual class FileHandler actual constructor(path: String) {

    private val file = File(path)

    actual fun read(): String {
        TODO("Not yet implemented")
    }

    actual fun write(content: String) {
    }
}