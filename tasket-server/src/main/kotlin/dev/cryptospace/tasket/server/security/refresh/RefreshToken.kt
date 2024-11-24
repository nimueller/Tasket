package dev.cryptospace.tasket.server.security.refresh

import java.time.OffsetDateTime

data class RefreshToken(val token: String, val expiration: OffsetDateTime)
