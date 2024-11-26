package dev.cryptospace.tasket.app.network

data class HttpResponse<T : Any>(val status: Short, val parsedEntity: T?)
