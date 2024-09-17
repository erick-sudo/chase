package org.slade.chase

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform