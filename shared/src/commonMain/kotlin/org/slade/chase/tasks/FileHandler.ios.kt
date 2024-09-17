package org.slade.chase.tasks

expect class FileHandler (
    path: String
) {
    fun read(): String
    fun write(content: String)
}