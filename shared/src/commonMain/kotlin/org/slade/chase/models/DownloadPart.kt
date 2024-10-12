package org.slade.chase.models

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface IDownloadPart {
    val id: String
    val index: Int
    val offset: Long
    val end: Long
    var retrieved: Long
}

//expect class DownloadPart: IDownloadPart

@Serializable
class DownloadPart @OptIn(ExperimentalUuidApi::class) constructor(
    override val id: String = Uuid.random().toString(),
    override val index: Int,
    override val offset: Long,
    override val end: Long,
    override var retrieved: Long = 0L
) : IDownloadPart