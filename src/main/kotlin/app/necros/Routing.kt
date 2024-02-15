package com.example.plugins.app.necros

import app.necros.DatabaseSingleton
import app.necros.model.Wallets
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.selectAll

fun Application.configureRouting() {
    routing {
        get("/wallet") {
            val res = DatabaseSingleton.dbQuery { 
                Wallets.selectAll().map {
                    mapOf(
                        Pair(Wallets.id.name, it[Wallets.id]),
                        Pair(Wallets.name.name, it[Wallets.name])
                    )
                }
            }
            call.respondText(res.toString(), status = HttpStatusCode.OK)
        }
    }
}
