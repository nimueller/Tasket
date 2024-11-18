package dev.cryptospace.tasket.server.routes

import PostgresIntegrationTest
import dev.cryptospace.module
import dev.cryptospace.tasket.payloads.TodoStatusPayload
import dev.cryptospace.tasket.types.BootstrapColor
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import kotlin.test.Test

@ExtendWith(PostgresIntegrationTest::class)
class TodoStatusTest {

    @Test
    fun `get all statuses should return default status list`() = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        client.get("/statuses").apply {
            assert(status == HttpStatusCode.OK)
            val payload = body<List<TodoStatusPayload>>()
            assert(payload.size == 4)
        }
    }

    @ParameterizedTest(name = "get specific status should return correct values for id {0}")
    @CsvFileSource(resources = ["/statuses.csv"], delimiter = ',')
    fun `get specific status should return correct values`(id: String, name: String, color: String) = testApplication {
        application {
            module()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        client.get("/statuses/$id").apply {
            assert(status == HttpStatusCode.OK)
            val payload = body<TodoStatusPayload>()
            assert(payload.id == id)
            assert(payload.name == name)
            assert(payload.color == BootstrapColor.valueOf(color))
        }
    }
}
