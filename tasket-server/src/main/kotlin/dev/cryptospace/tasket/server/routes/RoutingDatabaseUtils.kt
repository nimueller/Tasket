package dev.cryptospace.tasket.server.routes

import dev.cryptospace.tasket.payloads.Payload
import dev.cryptospace.tasket.server.table.PayloadMapper
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingContext
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.deleteReturning
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.upsert
import java.util.UUID

suspend inline fun <reified T, reified V> RoutingContext.handleGetAllRoute() where T : UUIDTable,
                                                                                   T : PayloadMapper<V>,
                                                                                   V : Payload {
    val table = requireNotNull(T::class.objectInstance)
    val todoPayloads =
        transaction {
            with(table) {
                selectAll().map { row ->
                    row.toPayload()
                }
            }
        }

    call.respond(todoPayloads)
}

suspend inline fun <reified T, reified V> RoutingContext.handleGetByIdRoute() where T : UUIDTable,
                                                                                    T : PayloadMapper<V>,
                                                                                    V : Payload {
    val table = requireNotNull(T::class.objectInstance)
    val payload =
        transaction {
            with(table) {
                selectAll().where {
                    id eq UUID.fromString(call.parameters["id"])
                }.singleOrNull()?.toPayload()
            }
        }

    if (payload == null) {
        call.respond(HttpStatusCode.NotFound)
    } else {
        call.respond(payload)
    }
}

suspend inline fun <reified T, reified V> RoutingContext.handlePostRoute() where T : UUIDTable,
                                                                                 T : PayloadMapper<V>,
                                                                                 V : Payload {
    val table = requireNotNull(T::class.objectInstance)
    val receivedPayload = call.receive<V>()
    val payload =
        transaction {
            with(table) {
                val insertedId =
                    insert { statement ->
                        statement.fromPayload(receivedPayload)
                    }[id]
                selectAll().where {
                    id eq insertedId
                }.single().toPayload()
            }
        }

    call.respond(payload)
}

suspend inline fun <reified T, reified V> RoutingContext.handleUpsertRoute() where T : UUIDTable,
                                                                                   T : PayloadMapper<V>,
                                                                                   V : Payload {
    val table = requireNotNull(T::class.objectInstance)
    val receivedPayload = call.receive<V>()
    val payload =
        transaction {
            addLogger(StdOutSqlLogger)

            with(table) {
                val insertedId =
                    upsert { statement ->
                        statement.fromPayload(receivedPayload)
                        statement[id] = UUID.fromString(call.parameters["id"])
                    }[id]

                selectAll().where {
                    id eq insertedId
                }.single().toPayload()
            }
        }

    call.respond(payload)
}

suspend inline fun <reified T, reified V> RoutingContext.handleDeleteRoute() where T : UUIDTable,
                                                                                   T : PayloadMapper<V>,
                                                                                   V : Payload {
    val table = requireNotNull(T::class.objectInstance)
    val deletedRowCount =
        transaction {
            with(table) {
                deleteReturning {
                    id eq UUID.fromString(call.parameters["id"])
                }.count()
            }
        }

    if (deletedRowCount == 0) {
        call.respond(HttpStatusCode.NotFound)
    } else {
        call.respond(HttpStatusCode.OK)
    }
}
