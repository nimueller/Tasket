package dev.cryptospace.tasket.logic

private const val MIN_PASSWORD_LENGTH = 8

object SecurePasswordValidator {

    enum class ValidationError {
        TOO_SHORT,
        NO_UPPERCASE,
        NO_LOWERCASE,
        NO_NUMBER,
        NO_SPECIAL_CHAR,
        NO_MATCH
    }

    fun isValid(password: String, passwordRepeat: String): Boolean {
        return validate(password, passwordRepeat).isEmpty()
    }

    fun validate(password: String, passwordRepeat: String): List<ValidationError> {
        val results = mutableListOf<ValidationError>()

        if (password.length < MIN_PASSWORD_LENGTH) {
            results.add(ValidationError.TOO_SHORT)
        }
        if (!password.any { it.isUpperCase() }) {
            results.add(ValidationError.NO_UPPERCASE)
        }
        if (!password.any { it.isLowerCase() }) {
            results.add(ValidationError.NO_LOWERCASE)
        }
        if (!password.any { it.isDigit() }) {
            results.add(ValidationError.NO_NUMBER)
        }
        if (!password.any { !it.isLetterOrDigit() }) {
            results.add(ValidationError.NO_SPECIAL_CHAR)
        }
        if (password.isEmpty() || password != passwordRepeat) {
            results.add(ValidationError.NO_MATCH)
        }

        return results
    }

}
