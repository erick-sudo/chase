package org.slade.chase.models

enum class TransferEncoding(value: String) {
    Chunked("chunked"),
    Raw("raw"),
    Range("range"),
}