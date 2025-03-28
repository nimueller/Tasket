package dev.cryptospace.tasket.server.routes

import dev.cryptospace.tasket.payloads.RequestPayload
import dev.cryptospace.tasket.payloads.ResponsePayload
import dev.cryptospace.tasket.server.payload.RequestMapper
import dev.cryptospace.tasket.server.repository.Repository
import dev.cryptospace.tasket.server.repository.UserScopedRepository
import dev.cryptospace.tasket.server.table.BaseTable
import dev.cryptospace.tasket.server.table.OwnedTable
import dev.cryptospace.tasket.server.utils.userId
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingContext
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import java.util.UUID

suspend inline fun <reified T : OwnedTable, reified RESP : ResponsePayload> RoutingContext.handleGetAllRoute(
    repository: UserScopedRepository<T, RESP>,
) {
    val todoPayloads = repository.getAllOwnedBy(call.userId())
    call.respond(todoPayloads)
}

suspend inline fun <reified T : BaseTable, reified RESP : ResponsePayload> RoutingContext.handleGetAllRoute(
    repository: Repository<T, RESP>,
) {
    val todoPayloads = repository.getAllIgnoreOwner()
    call.respond(todoPayloads)
}

suspend inline fun <reified T : OwnedTable, reified RESP : ResponsePayload> RoutingContext.handleGetByIdRoute(
    repository: UserScopedRepository<T, RESP>,
    id: UUID,
) {
    val payload = repository.getByIdAndOwner(id, call.userId())

    if (payload == null) {
        call.respond(HttpStatusCode.NotFound)
    } else {
        call.respond(payload)
    }
}

suspend inline fun <reified T : BaseTable, reified RESP : ResponsePayload> RoutingContext.handleGetByIdRoute(
    repository: Repository<T, RESP>,
    id: UUID,
) {
    val payload = repository.getByIdIgnoreOwner(id)

    if (payload == null) {
        call.respond(HttpStatusCode.NotFound)
    } else {
        call.respond(payload)
    }
}

suspend inline fun <reified T, reified REQ, reified RESP> RoutingContext.handlePostRoute(
    repository: Repository<T, RESP>,
    requestMapper: RequestMapper<T, REQ>,
    crossinline additionalAttributes: UpdateBuilder<Int>.() -> Unit = {},
) where T : BaseTable, REQ : RequestPayload, RESP : ResponsePayload {
    val receivedPayload = call.receive<REQ>()
    val payload = repository.insert {
        requestMapper.mapFromPayload(call.userId(), repository.table, receivedPayload, this)
        additionalAttributes(this)
    }
    call.respond(HttpStatusCode.Created, payload)
}

suspend inline fun <reified T, reified REQ, reified RESP> RoutingContext.handlePutRoute(
    repository: Repository<T, RESP>,
    requestMapper: RequestMapper<T, REQ>,
    id: UUID,
) where T : BaseTable, REQ : RequestPayload, RESP : ResponsePayload {
    validateExistingItemIsOwnedByUser(repository, id)
    val receivedPayload = call.receive<REQ>()
    val payload = repository.upsert(id) {
        requestMapper.mapFromPayload(call.userId(), repository.table, receivedPayload, this)
    }
    call.respond(payload)
}

suspend inline fun <reified T : BaseTable> RoutingContext.handleDeleteRoute(repository: Repository<T, *>, id: UUID) {
    validateExistingItemIsOwnedByUser(repository, id)
    val deletedRowCount = repository.delete(id)

    if (deletedRowCount == 0) {
        call.respond(HttpStatusCode.NotFound)
    } else {
        call.respond(HttpStatusCode.OK)
    }
}

suspend fun <T : BaseTable, RESP : ResponsePayload> RoutingContext.validateExistingItemIsOwnedByUser(
    repository: Repository<T, RESP>,
    id: UUID,
) {
    val item = repository.getByIdIgnoreOwner(id)
    val itemDoesNotExist = item == null

    if (itemDoesNotExist) {
        return
    }

    val itemHasNoOwner = item.metaInformation.ownerId == null
    val itemBelongsToOwner = item.metaInformation.ownerId == call.userId().toString()

    if (itemHasNoOwner || itemBelongsToOwner) {
        return
    }

    call.respond(HttpStatusCode.NotFound)
}
