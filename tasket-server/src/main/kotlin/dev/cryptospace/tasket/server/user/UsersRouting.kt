package dev.cryptospace.tasket.server.user

import dev.cryptospace.tasket.payloads.user.request.UserChangePasswordRequestPayload
import dev.cryptospace.tasket.server.authorisation.requireAdminOrOwnerPrivilege
import dev.cryptospace.tasket.server.authorisation.requireAdminPrivilege
import dev.cryptospace.tasket.server.routes.handleGetAllRoute
import dev.cryptospace.tasket.server.routes.handleGetByIdRoute
import dev.cryptospace.tasket.server.routes.handlePostRoute
import dev.cryptospace.tasket.server.routes.handlePutRoute
import dev.cryptospace.tasket.server.security.PasswordChangeHandler
import dev.cryptospace.tasket.server.table.user.UserId
import dev.cryptospace.tasket.server.user.database.UserRepository
import dev.cryptospace.tasket.server.user.mapper.UserRequestMapper
import dev.cryptospace.tasket.server.utils.userId
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.server.util.getOrFail
import java.util.UUID

fun Route.users() {
    route("users") {
        get {
            requireAdminPrivilege()
            handleGetAllRoute(UserRepository)
        }
        post {
            requireAdminPrivilege()
            handlePostRoute(UserRepository, UserRequestMapper)
        }
        get("/me") {
            val id = call.userId()
            val user = UserRepository.getByIdIgnoreOwner(id)

            if (user == null) {
                call.respond(message = "User not found", status = HttpStatusCode.NotFound)
            } else {
                call.respond(user)
            }
        }
        route("{userId}") {
            get {
                val id = call.parameters.getOrFail<UUID>(name = "userId")
                requireAdminOrOwnerPrivilege(id)
                handleGetByIdRoute(UserRepository, id)
            }
            put {
                val id = call.parameters.getOrFail<UUID>(name = "userId")
                requireAdminOrOwnerPrivilege(id)
                handlePutRoute(UserRepository, UserRequestMapper, id)
            }
            patch("change-password") {
                val id = UserId(call.parameters.getOrFail<UUID>(name = "userId"))
                requireAdminOrOwnerPrivilege(id)
                val changePasswordRequest = call.receive<UserChangePasswordRequestPayload>()
                val passwordChange = PasswordChangeHandler.tryChangePassword(
                    userId = id,
                    changePasswordRequest = changePasswordRequest,
                )

                if (passwordChange == null) {
                    call.respond(
                        message = "Password could not be changed",
                        status = HttpStatusCode.BadRequest
                    )
                    return@patch
                }

                call.respond(passwordChange)
            }
        }
    }
}
