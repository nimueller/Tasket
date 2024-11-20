package dev.cryptospace.tasket.server.routes

import dev.cryptospace.tasket.payloads.Payload
import dev.cryptospace.tasket.server.repository.BaseRepository
import dev.cryptospace.tasket.server.repository.ReadOnlyRepository
import dev.cryptospace.tasket.server.table.BaseTable
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingContext
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import java.util.UUID

suspend inline fun <reified T : BaseTable, reified V : Payload> RoutingContext.handleGetAllRoute(
    repository: ReadOnlyRepository<T, V>,
) {
    val todoPayloads = repository.getAll()
    call.respond(todoPayloads)
}

suspend inline fun <reified T : BaseTable, reified V : Payload> RoutingContext.handleGetByIdRoute(
    repository: ReadOnlyRepository<T, V>,
    id: UUID,
) {
    val payload = repository.getById(id)

    if (payload == null) {
        call.respond(HttpStatusCode.NotFound)
    } else {
        call.respond(payload)
    }
}

suspend inline fun <reified T : BaseTable, reified V : Payload> RoutingContext.handlePostRoute(
    repository: BaseRepository<T, V>,
    noinline additionalAttributes: UpdateBuilder<Int>.() -> Unit = {},
) {
    val receivedPayload = call.receive<V>()
    val payload = repository.insert(receivedPayload, additionalAttributes)
    call.respond(HttpStatusCode.Created, payload)
}

suspend inline fun <reified T : BaseTable, reified V : Payload> RoutingContext.handlePatchRoute(
    repository: BaseRepository<T, V>,
    id: UUID,
) {
    val receivedPayload = call.receive<V>()
    val payload = repository.upsert(receivedPayload, id)
    call.respond(payload)
}

suspend inline fun <reified T : BaseTable, reified V : Payload> RoutingContext.handleDeleteRoute(
    repository: BaseRepository<T, V>,
    id: UUID,
) {
    val deletedRowCount = repository.delete(id)

    if (deletedRowCount == 0) {
        call.respond(HttpStatusCode.NotFound)
    } else {
        call.respond(HttpStatusCode.OK)
    }
}
