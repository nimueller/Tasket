package dev.cryptospace.tasket.server.security.refresh

import com.auth0.jwt.JWT
import dev.cryptospace.tasket.payloads.authentication.RefreshTokenRequestPayload
import dev.cryptospace.tasket.server.security.JwtService
import dev.cryptospace.tasket.server.security.UserSessionService
import dev.cryptospace.tasket.server.table.user.RefreshTokensTable
import dev.cryptospace.tasket.server.table.user.UserId
import dev.cryptospace.tasket.server.utils.logger
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.OffsetDateTime
import java.util.UUID

object SecureRefresh {

    private val logger = logger<SecureRefresh>()

    fun tryRefresh(refreshTokenRequest: RefreshTokenRequestPayload): RefreshResult {
        val refreshToken = refreshTokenRequest.refreshToken

        if (!JwtService.verifyToken(refreshToken)) {
            // TODO log this failed attempt in database
            return FailedRefreshResult
        }

        return doRefresh(refreshToken)
    }

    private fun doRefresh(refreshToken: String): RefreshResult {
        val userId = extractUser(refreshToken) ?: return FailedRefreshResult
        val tokenValid = transaction {
            RefreshTokensTable.selectAll().where {
                RefreshTokensTable.userId eq userId and
                    (RefreshTokensTable.token eq refreshToken) and
                    (RefreshTokensTable.expiration greaterEq OffsetDateTime.now())
                // TODO maybe include device id in the query to make it more secure
            }.count() > 0
        }

        return if (tokenValid) {
            val session = UserSessionService.login(UserId(userId))
            SuccessRefreshResult(session)
        } else {
            // TODO log this failed attempt in database
            FailedRefreshResult
        }
    }

    private fun extractUser(refreshToken: String): UUID? {
        val decodedToken = JWT.decode(refreshToken)
        return try {
            UUID.fromString(decodedToken.subject)
        } catch (e: IllegalArgumentException) {
            // TODO log this failed attempt in database
            logger.error("Failed to parse UUID from refresh token subject", e)
            null
        }
    }
}
