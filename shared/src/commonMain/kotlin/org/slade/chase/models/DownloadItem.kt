package org.slade.chase.models

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class DownloadItem @OptIn(ExperimentalUuidApi::class) constructor(
    val id: String = Uuid.random().toString(),
    val source: String,
    var parts: List<DownloadPart> = emptyList()
)