package org.slade.chase

import org.slade.chase.models.DownloadItem
import org.slade.chase.models.DownloadState
import kotlin.math.floor

val byteUnits = listOf("B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB")

expect fun Double.suffixByteSize(): String

expect fun DownloadItem.inferState(): DownloadState

fun Long.ranges(n: Int = 2): List<Pair<Long, Long>> {
    require(this >= RANGE_THRESH_HOLD) { "A value greater than zero is required" }
    val posts = (0L..this).lineSpace(n.let { if(it >= 2) it else 2 } + 1)
    val result = mutableListOf<Pair<Long, Long>>()
    posts.forEachIndexed { index, post ->
        if(index < posts.size - 1) {
            val lowerLimit = floor(post).toLong()
            val upperLimit = floor(posts[index + 1]).toLong()
            result += lowerLimit to (upperLimit - 1)
        }
    }

    return result.toList()
}

fun LongRange.lineSpace(posts: Int = 2): List<Double> {
    require(posts >= 2) { "Number of posts must be greater or equal to 2" }

    val interval = (last - start)/(posts - 1).toDouble()

    val innerPosts = mutableListOf(start.toDouble())

    var count = 1L
    while (count <= posts - 2) {
        innerPosts += count * interval
        count++
    }

    innerPosts += last.toDouble()

    return innerPosts.toList()
}