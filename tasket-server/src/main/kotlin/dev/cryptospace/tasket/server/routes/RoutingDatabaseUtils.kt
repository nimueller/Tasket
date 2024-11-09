package dev.cryptospace.tasket.server.routes

import dev.cryptospace.tasket.payloads.Payload
import dev.cryptospace.tasket.server.repository.BaseRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingContext
import java.util.UUID

suspend inline fun <reified T : Payload> RoutingContext.handleGetAllRoute(repository: BaseRepository<T>) {
    val todoPayloads = repository.getAll()
    call.respond(todoPayloads)
}

suspend inline fun <reified T : Payload> RoutingContext.handleGetByIdRoute(repository: BaseRepository<T>, id: UUID) {
    val payload = repository.getById(id)

    if (payload == null) {
        call.respond(HttpStatusCode.NotFound)
    } else {
        call.respond(payload)
    }
}

suspend inline fun <reified T : Payload> RoutingContext.handlePostRoute(repository: BaseRepository<T>) {
    val receivedPayload = call.receive<T>()
    val payload = repository.insert(receivedPayload)
    call.respond(payload)
}

suspend inline fun <reified V : Payload> RoutingContext.handlePatchRoute(repository: BaseRepository<V>, id: UUID) {
    val receivedPayload = call.receive<V>()
    val payload = repository.upsert(receivedPayload, id)
    call.respond(payload)
}

suspend inline fun <reified V : Payload> RoutingContext.handleDeleteRoute(repository: BaseRepository<V>, id: UUID) {
    val deletedRowCount = repository.delete(id)

    if (deletedRowCount == 0) {
        call.respond(HttpStatusCode.NotFound)
    } else {
        call.respond(HttpStatusCode.OK)
    }
}
