package app.necros

import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals

class RouteTest {
    
    @Test
    fun `wallet get method respond ok`() = testApplication {
        val response = client.get("/wallet")
        assertEquals(HttpStatusCode.OK, response.status)
    }
}