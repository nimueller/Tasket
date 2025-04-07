package dev.cryptospace.tasket.payloads

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.modules.SerializersModule

val optionalFieldModule = SerializersModule {
    contextual(OptionalField::class) { typeArguments ->
        val argSerializer = typeArguments[0]
        @Suppress("UNCHECKED_CAST")
        OptionalFieldSerializer(argSerializer as KSerializer<Any?>) as KSerializer<OptionalField<*>>
    }
}

class OptionalFieldSerializer<T>(private val valueSerializer: KSerializer<T>) : KSerializer<OptionalField<T?>> {
    override val descriptor = buildClassSerialDescriptor("OptionalField") {
        element("value", valueSerializer.descriptor, isOptional = true)
    }

    override fun deserialize(decoder: Decoder): OptionalField<T?> {
        val jsonDecoder = decoder as? JsonDecoder ?: error("Only Json supported")
        val element = jsonDecoder.decodeJsonElement()

        return if (element is JsonNull) {
            OptionalField.Present(null)
        } else {
            val value = jsonDecoder.json.decodeFromJsonElement(valueSerializer, element)
            OptionalField.Present(value)
        }
    }

    override fun serialize(encoder: Encoder, value: OptionalField<T?>) {
        val jsonEncoder = encoder as? JsonEncoder ?: error("Only Json supported")

        when (value) {
            is OptionalField.Present -> {
                if (value.value == null) {
                    jsonEncoder.encodeJsonElement(JsonNull)
                } else {
                    @OptIn(ExperimentalSerializationApi::class)
                    jsonEncoder.encodeNullableSerializableValue(valueSerializer, value.value)
                }
            }

            is OptionalField.Missing -> {
                // Do nothing, as the field is missing
            }
        }
    }
}
