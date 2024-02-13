package com.example.plugins.app.necros

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/wallet") {
            call.respondText("No wallet found", status = HttpStatusCode.OK)
        }
    }
}
