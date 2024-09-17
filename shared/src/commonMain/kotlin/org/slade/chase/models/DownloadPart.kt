package org.slade.chase.models

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class DownloadPart (
    var id: String,
    var index: Int,
    var offset: Long,
    var end: Long,
)