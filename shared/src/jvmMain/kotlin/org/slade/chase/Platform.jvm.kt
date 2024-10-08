package org.slade.chase

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")} ${System.getProperty("user.home")}"
}

actual fun getPlatform(): Platform = JVMPlatform()