package app.necros

import app.necros.model.Wallets
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.testing.testApplication
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.Test
import kotlin.test.assertEquals

class RouteTest {
    
//    @BeforeTest
//    fun setup() {
//        testApplication { 
////            environment { 
////                config = ApplicationConfig("testapplication.conf")
////            }
//            
//        }
//    }
    
    @Test
    fun `wallet get method respond ok`() = testApplication {
        environment {
            config = ApplicationConfig("testapplication.conf")
        }
//        DatabaseSingleton.dbQuery {
//            Wallets.insert {
//                it[name] = "Cash"
//            }
//        }
        
        val response = client.get("/wallet")
        println(response.bodyAsText())
        assertEquals(HttpStatusCode.OK, response.status)
    }
}