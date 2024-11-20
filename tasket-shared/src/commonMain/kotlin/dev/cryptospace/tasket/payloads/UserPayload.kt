package dev.cryptospace.tasket.payloads

data class UserPayload(val username: String, val password: String?) : Payload()
