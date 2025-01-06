package dev.cryptospace.tasket.server.authorisation

import dev.cryptospace.tasket.server.user.database.UserRepository
import dev.cryptospace.tasket.server.user.database.UserRole
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.routing.RoutingContext
import java.util.UUID

suspend fun RoutingContext.requireAdminPrivilege() {
    if (isAdmin()) {
        return
    }

    throw AuthorisationException()
}

private suspend fun RoutingContext.isAdmin(): Boolean {
    val principal = checkNotNull(call.principal<UserIdPrincipal>())
    val principalUserId = UUID.fromString(principal.name)
    val userRole = UserRepository.getUserRole(principalUserId)
    return userRole == UserRole.ADMIN
}

suspend fun RoutingContext.requireOwnerPrivilege(userId: UUID) {
    val principal = checkNotNull(call.principal<UserIdPrincipal>())
    val principalUserId = UUID.fromString(principal.name)
    val isOwner = principalUserId == userId

    if (!isOwner) {
        throw AuthorisationException()
    }
}

suspend fun RoutingContext.requireAdminOrOwnerPrivilege(userId: UUID) {
    if (isAdmin()) {
        return
    }

    requireOwnerPrivilege(userId)
}
