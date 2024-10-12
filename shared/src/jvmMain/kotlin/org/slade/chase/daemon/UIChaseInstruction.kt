package org.slade.chase.daemon

import kotlinx.serialization.Serializable

@Serializable
data class UIChaseInstruction(
    var i: String
)
