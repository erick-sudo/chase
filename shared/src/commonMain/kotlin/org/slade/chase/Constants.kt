package org.slade.chase

const val SERVER_PORT = 8080

const val STATE_FILE_NAME = "state"

const val RANGE_THRESH_HOLD = 1024

val MimeTypes: Map<String, List<String>> = mapOf(
    "jpeg" to listOf("ffd8ffe0", "ffd8ffe1", "ffd8ffe2"),
    "png" to listOf("89504e47")
);

val MimeTypesMap: Map<String, String> by lazy {
    MimeTypes.entries.fold(emptyMap()) { acc, (mime, signatures) ->
        acc +signatures.map { signature -> signature to mime }
    }
}