package dev.cryptospace.tasket.server.security

import dev.cryptospace.tasket.logic.SecurePasswordValidator
import dev.cryptospace.tasket.payloads.user.request.UserChangePasswordRequestPayload
import dev.cryptospace.tasket.payloads.user.response.UserResponsePayload
import dev.cryptospace.tasket.server.security.login.SecureLogin
import dev.cryptospace.tasket.server.table.user.UserId
import dev.cryptospace.tasket.server.user.database.UserRepository

object PasswordChangeHandler {

    suspend fun tryChangePassword(
        userId: UserId,
        changePasswordRequest: UserChangePasswordRequestPayload
    ): UserResponsePayload? {
        return tryChangePassword(
            userId = userId,
            currentPassword = changePasswordRequest.currentPassword,
            newPassword = changePasswordRequest.newPassword,
            repeatNewPassword = changePasswordRequest.repeatNewPassword
        )
    }

    /**
     * Try to change the password of the user with the given [userId].
     * The [currentPassword] is used to check if the user is allowed to change the password.
     * The [newPassword] and [repeatNewPassword] are used to check if the new password is valid.
     * If the password change is successful, the new user object is returned.
     */
    suspend fun tryChangePassword(
        userId: UserId,
        currentPassword: String,
        newPassword: String,
        repeatNewPassword: String
    ): UserResponsePayload? {
        val isOldPasswordValid = SecureLogin.isPasswordValid(userId, currentPassword)
        val isNewPasswordValid = SecurePasswordValidator.isValid(
            newPassword,
            repeatNewPassword
        )

        if (!isNewPasswordValid || !isOldPasswordValid) {
            return null
        }

        val salt = Argon2Hashing.generateSalt()
        val hashedPassword = Argon2Hashing.hashPassword(newPassword, salt)

        return UserRepository.changePassword(userId, hashedPassword, salt)
    }
}
