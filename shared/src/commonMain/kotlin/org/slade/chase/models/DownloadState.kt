package org.slade.chase.models

enum class DownloadState(value: String) {
    Initializing("Initializing"),
    Downloading("Downloading"),
    Paused("Paused"),
    Assembling("Assembling"),
    Completed("Completed"),
}