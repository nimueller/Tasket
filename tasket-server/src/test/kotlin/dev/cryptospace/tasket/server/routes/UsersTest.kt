package dev.cryptospace.tasket.server.routes

import dev.cryptospace.tasket.payloads.user.request.UserRequestPayload
import dev.cryptospace.tasket.payloads.user.response.UserResponsePayload
import dev.cryptospace.tasket.server.user.database.UsersTable
import dev.cryptospace.tasket.test.PostgresIntegrationTest
import dev.cryptospace.tasket.test.testAuthenticatedWebservice
import dev.cryptospace.tasket.test.testWebservice
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
    fun `get all users should return 401 for unauthenticated request`() = testWebservice {
        val response = client.get("/rest/users")

        assert(response.status == HttpStatusCode.Unauthorized)
    }

    @Test
    fun `get all users should return 401 for normal user`() = testAuthenticatedWebservice(asAdmin = false) {
        val response = client.get("/rest/users")

        assert(response.status == HttpStatusCode.Unauthorized)
    }

    @Test
    fun `get user should return 401 for unauthenticated request`() = testWebservice {
        val response = client.get("/rest/users/00000000-0000-0000-0000-000000000000")

        assert(response.status == HttpStatusCode.Unauthorized)
    }

    @Test
    fun `get user should return 401 for normal user`() = testAuthenticatedWebservice(asAdmin = false) { user ->
        val response = client.get("/rest/users/${user.id}")

        assert(response.status == HttpStatusCode.Unauthorized)
    }

    @Test
    fun `put user should return 401 for unauthenticated request`() = testWebservice {
        val response = client.put("/rest/users/00000000-0000-0000-0000-000000000000")

        assert(response.status == HttpStatusCode.Unauthorized)
    }

    @Test
    fun `put user should return 401 for normal user`() = testAuthenticatedWebservice(asAdmin = false) { user ->
        val response = client.put("/rest/users/${user.id}")

        assert(response.status == HttpStatusCode.Unauthorized)
    }

    @Test
    fun `get all users should return valid result for admin user`() =
        testAuthenticatedWebservice(asUsername = "admin", asAdmin = true) {
            val response = client.get("/rest/users")

            assert(response.status == HttpStatusCode.OK)
            val users = response.body<List<UserResponsePayload>>()
            assert(users.size == 1)
            assert(users.first().username == "admin")
        }

    @Test
    fun `post user should insert user`() = testAuthenticatedWebservice(asAdmin = true) {
        val response = client.post("/rest/users") {
            contentType(ContentType.Application.Json)
            setBody(UserRequestPayload(username = "new", password = "password"))
        }

        assert(response.status == HttpStatusCode.Created)
        val insertedUser = transaction {
            UsersTable.selectAll().where { UsersTable.username eq "new" }.single()
        }
        assert(insertedUser[UsersTable.username] == "new")
    }

    @Test
    fun `get after post user should return inserted user`() = testAuthenticatedWebservice(asAdmin = true) {
        val insertedId = client.post("/rest/users") {
            contentType(ContentType.Application.Json)
            setBody(UserRequestPayload(username = "new", password = "password"))
        }.apply {
            assert(status == HttpStatusCode.Created)
        }.body<UserResponsePayload>().metaInformation.id

        client.get("/rest/users/$insertedId").apply {
            assert(status == HttpStatusCode.OK)
            val user = body<UserResponsePayload>()
            assert(user.username == "new")
        }
    }

    @Test
    fun `put user should update user`() = testAuthenticatedWebservice(asAdmin = true) {
        val insertedId = client.post("/rest/users") {
            contentType(ContentType.Application.Json)
            setBody(UserRequestPayload(username = "new", password = "password"))
        }.apply {
            assert(status == HttpStatusCode.Created)
        }.body<UserResponsePayload>().metaInformation.id

        client.put("/rest/users/$insertedId") {
            contentType(ContentType.Application.Json)
            setBody(UserRequestPayload(username = "updated", password = "password"))
        }.apply {
            assert(status == HttpStatusCode.OK)
            val updatedUser = transaction {
                UsersTable.selectAll().where { UsersTable.id eq UUID.fromString(insertedId) }.single()
            }
            assert(updatedUser[UsersTable.username] == "updated")
        }
    }
}
