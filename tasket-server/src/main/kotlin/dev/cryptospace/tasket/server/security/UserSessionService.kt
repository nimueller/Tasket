package dev.cryptospace.tasket.server.security

import dev.cryptospace.tasket.payloads.authentication.LoginResponsePayload
import dev.cryptospace.tasket.server.table.user.RefreshTokensTable
import dev.cryptospace.tasket.server.table.user.UserId
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

object UserSessionService {
    fun login(userUuid: UserId): LoginResponsePayload {
        val accessToken = JwtService.generateAccessToken(userUuid)
        val refreshToken = JwtService.generateRefreshToken(userUuid)

        transaction {
            RefreshTokensTable.insert {
                it[userId] = userUuid.value
                it[token] = refreshToken.token
                it[expiration] = refreshToken.expiration
            }
        }

        return LoginResponsePayload(accessToken = accessToken, refreshToken = refreshToken.token)
    }
}
