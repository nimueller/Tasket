package dev.cryptospace.tasket.payloads

import kotlinx.serialization.Serializable

@Serializable
sealed class OptionalField<out T : Any?> {
    object Missing : OptionalField<Nothing>()

    data class Present<T>(val value: T?) : OptionalField<T>()

    companion object {

        fun <T> empty(): OptionalField<T> {
            return Missing
        }

        fun <T> of(value: T): OptionalField<T> {
            return Present(value)
        }
    }
}
