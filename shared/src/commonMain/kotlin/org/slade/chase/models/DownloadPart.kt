package org.slade.chase.models

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface IDownloadPart {
    val id: String
    val index: Int
    val offset: Long
    val end: Long
    var retrieved: Long
}

expect class DownloadPart: IDownloadPart