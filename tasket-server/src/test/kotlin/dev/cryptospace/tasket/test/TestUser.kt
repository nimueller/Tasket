package dev.cryptospace.tasket.test

import dev.cryptospace.tasket.server.table.user.UserId

data class TestUser(
    val id: UserId,
    val username: String,
    val password: String
)
