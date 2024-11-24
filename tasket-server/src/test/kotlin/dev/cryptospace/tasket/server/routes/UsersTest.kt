package dev.cryptospace.tasket.server.routes

import dev.cryptospace.tasket.payloads.UserPayload
import dev.cryptospace.tasket.server.table.user.UsersTable
import dev.cryptospace.tasket.test.PostgresIntegrationTest
import dev.cryptospace.tasket.test.testWebserviceAuthenticated
import dev.cryptospace.tasket.test.testWebserviceAuthenticatedWithUser
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.extension.ExtendWith
import java.util.UUID
import kotlin.test.Test

@ExtendWith(PostgresIntegrationTest::class)
class UsersTest {

    @Test
    fun `get all users should return 401 for normal user`() = testWebserviceAuthenticated(isAdmin = false) {
        get("/users").apply {
            assert(status == HttpStatusCode.Unauthorized)
        }
    }

    @Test
    fun `post user should return 401 for normal user`() = testWebserviceAuthenticated(isAdmin = false) {
        post("/users").apply {
            assert(status == HttpStatusCode.Unauthorized)
        }
    }

    @Test
    fun `get user should return 401 for normal user`() = testWebserviceAuthenticatedWithUser(isAdmin = false) { user ->
        get("/users/${user.id}").apply {
            assert(status == HttpStatusCode.Unauthorized)
        }
    }

    @Test
    fun `put user should return 401 for normal user`() = testWebserviceAuthenticatedWithUser(isAdmin = false) { user ->
        put("/users/${user.id}").apply {
            assert(status == HttpStatusCode.Unauthorized)
        }
    }

    @Test
    fun `get all users should return valid result for admin user`() = testWebserviceAuthenticated(isAdmin = true) {
        get("/users").apply {
            assert(status == HttpStatusCode.OK)
            val users = body<List<UserPayload>>()
            assert(users.size == 1)
            assert(users.map { it.username }.containsAll(listOf("test")))
        }
    }

    @Test
    fun `post user should insert user`() = testWebserviceAuthenticated(isAdmin = true) {
        post("/users") {
            contentType(ContentType.Application.Json)
            setBody(UserPayload(username = "new", password = "new"))
        }.apply {
            assert(status == HttpStatusCode.Created)
            val insertedUser = transaction {
                UsersTable.selectAll().where { UsersTable.username eq "new" }.single()
            }
            assert(insertedUser[UsersTable.username] == "new")
        }
    }

    @Test
    fun `get after post user should return inserted user`() = testWebserviceAuthenticated(isAdmin = true) {
        val insertedId = post("/users") {
            contentType(ContentType.Application.Json)
            setBody(UserPayload(username = "new", password = "new"))
        }.apply {
            assert(status == HttpStatusCode.Created)
        }.body<UserPayload>().id

        get("/users/$insertedId").apply {
            assert(status == HttpStatusCode.OK)
            val user = body<UserPayload>()
            assert(user.username == "new")
        }
    }

    @Test
    fun `put user should update user`() = testWebserviceAuthenticated(isAdmin = true) {
        val insertedId = post("/users") {
            contentType(ContentType.Application.Json)
            setBody(UserPayload(username = "new", password = "new"))
        }.apply {
            assert(status == HttpStatusCode.Created)
        }.body<UserPayload>().id

        put("/users/$insertedId") {
            contentType(ContentType.Application.Json)
            setBody(UserPayload(username = "updated", password = "updated"))
        }.apply {
            assert(status == HttpStatusCode.OK)
            val updatedUser = transaction {
                UsersTable.selectAll().where { UsersTable.id eq UUID.fromString(insertedId) }.single()
            }
            assert(updatedUser[UsersTable.username] == "updated")
        }
    }
}
