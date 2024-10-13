package org.slade.chase

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import java.util.concurrent.TimeUnit

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")} ${System.getProperty("user.home")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

actual fun httpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(OkHttp) {
    config(this)

    engine {
        config {
            retryOnConnectionFailure(true)
            connectTimeout(0, TimeUnit.SECONDS)
        }
    }
}