package org.slade.chase

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.options
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

val client = HttpClient(OkHttp) {
    install(ContentNegotiation) {
        json()
    }
}

@Serializable
data class Customer(
    val id: Int,
    val firstName: String,
    val lastName: String
)

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}


fun Application.module() {
    routing {
        get("/") {
            client.options(
                "http://localhost:3000/"
            )
            call.respondText("Ktor: ${Greeting().greet()}")
        }

        get("/success") {
            call.respondText("Download Success")
        }
    }
}