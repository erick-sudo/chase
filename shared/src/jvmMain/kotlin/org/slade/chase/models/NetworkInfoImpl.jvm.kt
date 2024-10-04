package org.slade.chase.models

import java.net.Inet4Address

actual class NetworkInfoImpl actual constructor() : INetworkInfo {
    override var ip: ByteArray = byteArrayOf(127, 23, 1, 10)
    override var macAddress: String = Inet4Address.getLoopbackAddress().toString()
}