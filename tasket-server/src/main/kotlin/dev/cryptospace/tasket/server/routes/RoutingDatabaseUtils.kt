package dev.cryptospace.tasket.server.routes

import dev.cryptospace.tasket.payloads.PatchRequestPayload
import dev.cryptospace.tasket.payloads.RequestPayload
import dev.cryptospace.tasket.payloads.ResponsePayload
import dev.cryptospace.tasket.server.payload.PatchRequestMapper
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

/**
 * Handles a GET request for a given [repository]. It will respond with all items in the database that belong to the
 * current user.
 */
suspend inline fun <reified T : OwnedTable, reified RESP : ResponsePayload> RoutingContext.handleGetAllRoute(
    repository: UserScopedRepository<T, RESP>,
) {
    val todoPayloads = repository.getAllOwnedBy(call.userId())
    call.respond(todoPayloads)
}

/**
 * Handles a GET request for a given [repository]. It will respond with all items in the database.
 */
suspend inline fun <reified T : BaseTable, reified RESP : ResponsePayload> RoutingContext.handleGetAllRoute(
    repository: Repository<T, RESP>,
) {
    val todoPayloads = repository.getAllIgnoreOwner()
    call.respond(todoPayloads)
}

/**
 * Handles a GET request for a given [repository] and [id]. It will try to find the item by [id] in the database and
 * respond with the item if it exists. If the item does not exist, it will respond with a 404. If the found item does
 * not belong to the current user, it will also respond with a 404.
 */
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

/**
 * Handles a GET request for a given [repository] and [id]. It will try to find the item by [id] in the database and
 * respond with the item if it exists. If the item does not exist, it will respond with a 404.
 */
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

/**
 * Handles a POST request for a given [repository] and [requestMapper]. Optionally, additional attributes can be set
 * on the [UpdateBuilder] using the [additionalAttributes] lambda.
 */
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

/**
 * Handles a PUT request for a given [repository] and [requestMapper]. It will try to find the item by [id] in the
 * database and update it with the new values from the request. If the item does not exist, it will create a new item
 * with the given [id]. If the found item has an owner, it will check if the item belongs to the current user. If it
 * does not, it will respond with a 404.
 */
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

suspend inline fun <reified T, reified REQ, reified RESP> RoutingContext.handlePatchRoute(
    repository: Repository<T, RESP>,
    requestMapper: PatchRequestMapper<T, REQ>,
    id: UUID,
) where T : BaseTable, REQ : PatchRequestPayload, RESP : ResponsePayload {
    val item = repository.getByIdIgnoreOwner(id)
    if (item == null) {
        call.respond(HttpStatusCode.NotFound)
        return
    }
    validateExistingItemIsOwnedByUser(repository, id)
    val receivedPayload = call.receive<REQ>()
    val payload = repository.update(id) {
        requestMapper.mapFromPayload(call.userId(), repository.table, receivedPayload, this)
    }
    call.respond(payload)
}

/**
 * Handles a DELETE request for a given [repository] and [id]. It will try to find the item by [id] in the database and
 * delete it. If the item does not exist, it will respond with a 404. If the found item has an owner, it will check if
 * the item belongs to the current user. If it does not, it will respond with a 404.
 */
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
    validateExistingItemIsOwnedByUser(item)
}

/**
 * Validates that an item with the given [id] exists in the database and that it belongs to the current user.
 * If the item does not exist, it will return early. If the item has no owner or belongs to the current user, it will
 * return early. Otherwise, it will respond with a 404.
 */
suspend fun <RESP : ResponsePayload> RoutingContext.validateExistingItemIsOwnedByUser(item: RESP?) {
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
