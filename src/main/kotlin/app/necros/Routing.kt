package app.necros

import app.necros.model.Wallets
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.selectAll

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("❤\uFE0F❤\uFE0F❤\uFE0F I LOVE YOUUUU ❤\uFE0F❤\uFE0F❤\uFE0F", status = HttpStatusCode.OK)
        }
        get("/test-wallet") {
            val res = DatabaseSingleton.dbQuery {
                Wallets.selectAll().map {
                    mapOf(
                        Pair(Wallets.id.name, it[Wallets.id]),
                        Pair(Wallets.name.name, it[Wallets.name])
                    )
                }
            }
            call.respondText("TESTING TESTING TESTING123123123 $res", status = HttpStatusCode.OK)
        }
        
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
