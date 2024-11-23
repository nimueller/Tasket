package dev.cryptospace.tasket.server.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import dev.cryptospace.Environment
import dev.cryptospace.tasket.server.table.UserId
import org.slf4j.LoggerFactory
import java.time.Instant

const val AUDIENCE = "user"
const val REALM = "tasket"

private const val DEFAULT_JWT_DURATION_IN_MILLIS = 3600L * 1000L

object JwtService {
    private val logger = LoggerFactory.getLogger(JwtService::class.java)

    fun generateToken(userId: UserId, expiresInMillis: Long = DEFAULT_JWT_DURATION_IN_MILLIS): String {
        val algorithm = Algorithm.HMAC256(Environment.JWT_SECRET)
        return JWT.create()
            .withIssuer(Environment.HOST)
            .withAudience(AUDIENCE)
            .withSubject(userId.value)
            .withExpiresAt(Instant.now().plusMillis(expiresInMillis))
            .sign(algorithm)
    }

    fun verifyToken(token: String): Boolean {
        val verifier = JWT.require(Algorithm.HMAC256(Environment.JWT_SECRET))
            .withIssuer(Environment.HOST)
            .withAudience(AUDIENCE)
            .build()

        // Catching all exceptions is intentional in this security sensitive context
        @Suppress("TooGenericExceptionCaught")
        return try {
            verifier.verify(token)
            true
        } catch (e: Exception) {
            logger.error(e.message, e)
            false
        }
    }
}
