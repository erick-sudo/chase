package org.slade.chase.models

import java.io.Serializable
import java.util.UUID

actual class DownloadPart(
    override val id: String = UUID.randomUUID().toString(),
    override val index: Int,
    override val offset: Long,
    override val end: Long,
    override var retrieved: Long = 0L
) : IDownloadPart, Serializable