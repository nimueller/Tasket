package dev.cryptospace.tasket.test

import dev.cryptospace.tasket.server.security.Argon2Hashing
import dev.cryptospace.tasket.server.table.user.UserId
import io.ktor.util.encodeBase64
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.statements.StatementType
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID
import kotlin.test.assertNotNull

/**
 * Insert a user into the database. The user will have the specified [username] and the respective [password].
 * If [isAdmin] is set to true, the user will be an admin user. Otherwise, the user will be a regular user.
 * This function returns a [TestUser] object that contains the user's id, username, and password to be used in
 * further tests.
 */
fun insertUser(
    username: String = TEST_USER_USERNAME,
    password: String = TEST_USER_PASSWORD,
    isAdmin: Boolean = false,
): TestUser {
    val passwordSalt = Argon2Hashing.generateSalt()
    val passwordHash = Argon2Hashing.hashPassword(password, passwordSalt).encodeBase64()

    val testUserId = transaction {
        // of course there is an SQL injection vulnerability here, but it's good enough for testing,
        // as string interpolation is more readable
        val insertedId = doInsert(username, passwordHash, passwordSalt.encodeBase64(), isAdmin)

        assertNotNull(insertedId)
        UserId(UUID.fromString(insertedId))
    }

    return TestUser(id = testUserId, username = username, password = password)
}

private fun Transaction.doInsert(
    username: String,
    passwordHash: String,
    passwordSalt: String,
    isAdmin: Boolean,
): String? {
    return exec(
        stmt = """
                INSERT INTO tasket.users (username, password, salt, user_role)
                VALUES (
                    '$username',
                    '$passwordHash',
                    '$passwordSalt',
                    '${if (isAdmin) "ADMIN" else "USER"}'
                ) RETURNING id;
            """,
        // the INSERT statement returns the id of the inserted row, so hint a return value to Exposed, otherwise
        // it will implicitly only assume an INSERT without a return value
        explicitStatementType = StatementType.SELECT,
        transform = { resultSet ->
            assert(resultSet.next())
            resultSet.getString("id")
        },
    )
}
