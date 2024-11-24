package dev.cryptospace.tasket.server.security.login

import dev.cryptospace.tasket.payloads.authentication.LoginRequestPayload
import dev.cryptospace.tasket.server.security.Argon2Hashing
import dev.cryptospace.tasket.server.security.UserSessionService
import dev.cryptospace.tasket.server.table.user.UserId
import dev.cryptospace.tasket.server.table.user.UsersTable
import io.ktor.util.decodeBase64Bytes
import io.ktor.util.encodeBase64
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.security.MessageDigest

object SecureLogin {
    /**
     * Dummy salt used for comparing passwords even if the user does not exist to prevent timing attacks.
     * The salt is base64 encoded as the database stores the salt in base64 and has to decode first.
     */
    private val DUMMY_SALT = Argon2Hashing.generateSalt().encodeBase64()

    /**
     * Dummy hash used for comparing passwords even if the user does not exist to prevent timing attacks.
     * The password is base64 encoded as the database stores the password in base64 and has to decode first.
     */
    private val DUMMY_PASSWORD = Argon2Hashing.hashPassword("dummy", DUMMY_SALT.decodeBase64Bytes()).encodeBase64()

    fun tryLogin(loginRequest: LoginRequestPayload): LoginResult {
        val user = transaction {
            UsersTable.selectAll().where { UsersTable.username eq loginRequest.username }.singleOrNull()
        }

        // Even if the user does not exist, we compare the password to a dummy to prevent timing attacks.
        // Do not early return here.
        val salt = user?.let { it[UsersTable.salt] }?.decodeBase64Bytes() ?: DUMMY_SALT.decodeBase64Bytes()
        val requestPassword = Argon2Hashing.hashPassword(loginRequest.password, salt)
        val expectedPassword = user?.get(UsersTable.password)?.decodeBase64Bytes() ?: DUMMY_PASSWORD.decodeBase64Bytes()

        return if (MessageDigest.isEqual(requestPassword, expectedPassword)) {
            val userUuid = requireNotNull(user) {
                "User should not be null here, as we already checked the password."
            }[UsersTable.id].value
            val session = UserSessionService.login(UserId(userUuid))
            SuccessLoginResult(session)
        } else {
            FailedLoginResult
        }
    }
}
