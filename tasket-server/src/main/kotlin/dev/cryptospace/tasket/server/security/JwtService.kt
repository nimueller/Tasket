package dev.cryptospace.tasket.server.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import dev.cryptospace.Environment
import dev.cryptospace.tasket.server.security.refresh.RefreshToken
import dev.cryptospace.tasket.server.table.user.UserId
import dev.cryptospace.tasket.server.utils.logger
import java.time.Instant
import java.time.ZoneOffset

object JwtService {
    const val AUDIENCE = "user"
    const val REALM = "tasket"

    private const val ACCESS_TOKEN_EXPIRATION = 15L * 60L * 1000L
    private const val REFRESH_TOKEN_EXPIRATION = 30L * 24L * 60L * 60L * 1000L

    private val logger = logger<JwtService>()
    private val algorithm = Algorithm.HMAC256(Environment.JWT_SECRET)

    fun generateAccessToken(userId: UserId): String {
        return JWT.create()
            .withIssuer(Environment.HOST)
            .withAudience(AUDIENCE)
            .withSubject(userId.value.toString())
            .withExpiresAt(Instant.now().plusMillis(ACCESS_TOKEN_EXPIRATION))
            .sign(algorithm)
    }

    fun generateRefreshToken(userId: UserId): RefreshToken {
        val expiration = Instant.now().plusMillis(REFRESH_TOKEN_EXPIRATION)
        val token = JWT.create()
            .withIssuer(Environment.HOST)
            .withAudience(AUDIENCE)
            .withSubject(userId.value.toString())
            .withClaim("type", "refresh")
            .withExpiresAt(expiration)
            .sign(algorithm)

        return RefreshToken(token, expiration.atOffset(ZoneOffset.UTC))
    }

    fun verifyToken(token: String): Boolean {
        // Catching all exceptions is intentional in this security sensitive context
        @Suppress("TooGenericExceptionCaught")
        return try {
            JWT.require(algorithm)
                .withIssuer(Environment.HOST)
                .withAudience(AUDIENCE)
                .build()
                .verify(token)
            true
        } catch (e: Exception) {
            logger.error(e.message, e)
            false
        }
    }
}
