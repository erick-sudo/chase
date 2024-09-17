package org.slade.chase

import java.net.URL
import java.nio.file.Path

fun Path.fileName(): String {
    return  if(nameCount > 0) getName(nameCount - 1).toString() else ""
}

fun URL.fileName(): String {
    return Path.of(path).fileName()
}