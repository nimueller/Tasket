package dev.cryptospace.tasket.server.security

import org.bouncycastle.crypto.generators.Argon2BytesGenerator
import org.bouncycastle.crypto.params.Argon2Parameters
import java.security.SecureRandom

private const val SALT_LENGTH = 16
private const val HASH_LENGTH = 32
private const val ARGON_PARALLELISM = 1
private const val ARGON_MEMORY_IN_KB = 65536
private const val ARGON_ITERATIONS = 3

object Argon2Hashing {
    fun hashPassword(password: String, salt: ByteArray): ByteArray {
        val argon2 = Argon2BytesGenerator()
        val params = Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
            .withSalt(salt)
            .withParallelism(ARGON_PARALLELISM)
            .withMemoryAsKB(ARGON_MEMORY_IN_KB)
            .withIterations(ARGON_ITERATIONS)
            .build()

        argon2.init(params)

        val hash = ByteArray(HASH_LENGTH)
        argon2.generateBytes(password.toByteArray(Charsets.UTF_8), hash, 0, HASH_LENGTH)
        return hash
    }

    fun generateSalt(): ByteArray {
        val salt = ByteArray(SALT_LENGTH)
        SecureRandom().nextBytes(salt)
        return salt
    }
}
