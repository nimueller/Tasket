package dev.cryptospace.tasket.server.routes

import dev.cryptospace.tasket.payloads.Payload
import dev.cryptospace.tasket.server.payload.PayloadToDatabaseMapper
import dev.cryptospace.tasket.server.repository.BaseRepository
import dev.cryptospace.tasket.server.table.BaseTable
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingContext
import java.util.UUID

suspend inline fun <reified T : BaseTable, reified P : Payload> RoutingContext.handleGetAllRoute(
    repository: BaseRepository<T, P>,
) {
    val todoPayloads = repository.getAll()
    call.respond(todoPayloads)
}

suspend inline fun <reified T : BaseTable, reified P : Payload> RoutingContext.handleGetByIdRoute(
    repository: BaseRepository<T, P>,
    id: UUID,
) {
    val payload = repository.getById(id)

    if (payload == null) {
        call.respond(HttpStatusCode.NotFound)
    } else {
        call.respond(payload)
    }
}

suspend inline fun <reified T : BaseTable, reified P : Payload> RoutingContext.handlePostRoute(
    repository: BaseRepository<T, P>,
) {
    val receivedPayload = call.receive<P>()
    val payload = repository.insert(receivedPayload)
    call.respond(payload)
}

suspend inline fun <reified T : BaseTable, reified P : Payload> RoutingContext.handlePutRoute(
    repository: BaseRepository<T, P>,
    id: UUID,
) {
    val receivedPayload = call.receive<P>()
    val payload = repository.upsert(receivedPayload, id)
    call.respond(payload)
}

suspend inline fun <reified T : BaseTable, reified P : Payload> RoutingContext.handlePatchRoute(
    repository: BaseRepository<T, *>,
    id: UUID,
    patchPayloadMapper: PayloadToDatabaseMapper<T, P>,
) {
    val receivedPayload = call.receive<P>()
    val payload = repository.patch(receivedPayload, id, patchPayloadMapper)
    call.respond(payload)
}

suspend inline fun <reified T : BaseTable, reified P : Payload> RoutingContext.handleDeleteRoute(
    repository: BaseRepository<T, P>,
    id: UUID,
) {
    val deletedRowCount = repository.delete(id)

    if (deletedRowCount == 0) {
        call.respond(HttpStatusCode.NotFound)
    } else {
        call.respond(HttpStatusCode.OK)
    }
}
