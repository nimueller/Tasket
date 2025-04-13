package dev.cryptospace.tasket.server.security.refresh

import com.auth0.jwt.JWT
import dev.cryptospace.tasket.payloads.authentication.RefreshTokenRequestPayload
import dev.cryptospace.tasket.server.security.JwtService
import dev.cryptospace.tasket.server.security.UserSessionService
import dev.cryptospace.tasket.server.table.user.RefreshTokensTable
import dev.cryptospace.tasket.server.table.user.UserId
import dev.cryptospace.tasket.server.utils.logger
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.OffsetDateTime
import java.util.UUID

object SecureRefresh {

    private val logger = logger<SecureRefresh>()

    suspend fun tryRefresh(refreshTokenRequest: RefreshTokenRequestPayload): RefreshResult {
        val refreshToken = refreshTokenRequest.refreshToken

        if (!JwtService.verifyToken(refreshToken)) {
            return FailedRefreshResult
        }

        return doRefresh(refreshToken)
    }

    private suspend fun doRefresh(refreshToken: String): RefreshResult {
        val userId = extractUser(refreshToken) ?: return FailedRefreshResult
        val tokenValid = verifyTokenValid(userId, refreshToken)

        return if (tokenValid) {
            val session = UserSessionService.login(UserId(userId))
            deleteOutdatedRefreshToken(userId, refreshToken)
            SuccessRefreshResult(session)
        } else {
            FailedRefreshResult
        }
    }

    private fun verifyTokenValid(userId: UUID, refreshToken: String): Boolean = transaction {
        RefreshTokensTable.selectAll().where {
            RefreshTokensTable.userId eq userId and
                (RefreshTokensTable.token eq refreshToken) and
                (RefreshTokensTable.expiration greaterEq OffsetDateTime.now())
        }.count() > 0
    }

    private fun deleteOutdatedRefreshToken(user: UUID, refreshToken: String) = transaction {
        RefreshTokensTable.deleteWhere {
            userId eq user and (token eq refreshToken)
        }
    }

    private fun extractUser(refreshToken: String): UUID? {
        val decodedToken = JWT.decode(refreshToken)
        return try {
            UUID.fromString(decodedToken.subject)
        } catch (e: IllegalArgumentException) {
            logger.error("Failed to parse UUID from refresh token subject", e)
            null
        }
    }
}
