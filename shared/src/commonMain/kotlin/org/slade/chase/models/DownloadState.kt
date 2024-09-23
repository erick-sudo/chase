package org.slade.chase.models

enum class DownloadState(value: String) {
    Initializing("Initializing"),
    Starting("Starting"),
    Resumed("Resumed"),
    Cancelled("Cancelled"),
    Pausing("Pausing"),
    Paused("Paused"),
    Stopping("Stopping"),
    Stopped("Stopped"),
    Assembling("Assembling"),
    Completed("Completed"),
}