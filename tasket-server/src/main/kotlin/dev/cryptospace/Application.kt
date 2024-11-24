package dev.cryptospace

import com.auth0.jwt.JWT
import dev.cryptospace.tasket.server.routes.login
import dev.cryptospace.tasket.server.routes.status
import dev.cryptospace.tasket.server.routes.todo
import dev.cryptospace.tasket.server.routes.users
import dev.cryptospace.tasket.server.security.JwtService
import dev.cryptospace.tasket.server.table.user.UserRole
import dev.cryptospace.tasket.server.table.user.UsersTable
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.bearer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.routing.routing
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

fun main() {
    initializeDatabase()
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module).start(wait = true)
}

fun Application.module() {
    install(Authentication) {
        installBearerAuthentication()
        installAdminBearerAuthentication()
    }
    install(ContentNegotiation) {
        json()
    }
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        if (System.getenv("ALLOWED_HOST") != null) allowHost(System.getenv("ALLOWED_HOST")) else anyHost()
    }
    routing {
        login()

        authenticate {
            status()
            todo()
        }

        authenticate("admin") {
            users()
        }
    }
}

private fun AuthenticationConfig.installBearerAuthentication() {
    bearer {
        realm = JwtService.REALM
        authenticate { tokenCredential ->
            val token = tokenCredential.token
            if (JwtService.verifyToken(token)) {
                UserIdPrincipal(JWT.decode(token).subject)
            } else {
                null
            }
        }
    }
}

private fun AuthenticationConfig.installAdminBearerAuthentication() {
    bearer("admin") {
        realm = JwtService.REALM
        authenticate { tokenCredential ->
            val token = tokenCredential.token
            if (JwtService.verifyToken(token)) {
                val userId = JWT.decode(token).subject
                val isAdmin = transaction {
                    UsersTable.selectAll().where {
                        UsersTable.id eq UUID.fromString(userId) and
                            (UsersTable.role eq UserRole.ADMIN)
                    }.count() > 0
                }

                if (isAdmin) {
                    UserIdPrincipal(userId)
                } else {
                    null
                }
            } else {
                null
            }
        }
    }
}
