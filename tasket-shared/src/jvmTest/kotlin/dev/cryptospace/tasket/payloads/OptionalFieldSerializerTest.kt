package dev.cryptospace.tasket.payloads

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.test.BeforeTest
import kotlin.test.Test

class OptionalFieldSerializerTest {

    private lateinit var json: Json

    @BeforeTest
    fun setup() {
        json = Json {
            serializersModule = optionalFieldModule
        }
    }

    @Serializable
    data class OptionalFieldPayload(
        @Contextual val label: OptionalField<String?> = OptionalField.Missing,
        @Contextual val type: OptionalField<Int?> = OptionalField.Missing,
    )

    @Test
    fun serializationShouldIncludeAllFields() {
        val payload = OptionalFieldPayload(
            label = OptionalField.Present("Test"),
            type = OptionalField.Present(123),
        )
        val expectedJson = """
            {
                "label": "Test",
                "type": 123
            }
        """.replace("\\s".toRegex(), "")

        val result = json.encodeToString(OptionalFieldPayload.serializer(), payload)

        assert(result == expectedJson)
    }

    @Test
    fun serializationShouldIncludePresentField() {
        val payload = OptionalFieldPayload(label = OptionalField.Present("Test"))
        val expectedJson = """
            {
                "label": "Test"
            }
        """.replace("\\s".toRegex(), "")

        val result = json.encodeToString(OptionalFieldPayload.serializer(), payload)

        assert(result == expectedJson)
    }

    @Test
    fun serializationShouldIncludeNullField() {
        val payload = OptionalFieldPayload(
            label = OptionalField.Present("Test"),
            type = OptionalField.Present(null),
        )
        val expectedJson = """
            {
                "label": "Test",
                "type": null
            }
        """.replace("\\s".toRegex(), "")

        val result = json.encodeToString(OptionalFieldPayload.serializer(), payload)

        assert(result == expectedJson)
    }

    @Test
    fun serializationShouldNotIncludeMissingField() {
        val payload = OptionalFieldPayload()
        val expectedJson = """
            {}
        """.replace("\\s".toRegex(), "")

        val result = json.encodeToString(OptionalFieldPayload.serializer(), payload)

        assert(result == expectedJson)
    }

    @Test
    fun deserializationShouldIgnoreMissingField() {
        val payload = """
            {
                "label": "Test"
            }
        """

        val result = json.decodeFromString<OptionalFieldPayload>(payload)

        assert((result.label as OptionalField.Present).value == "Test")
    }

    @Test
    fun deserializationShouldIncludeNullField() {
        val payload = """
            {
                "label": null
            }
        """.trimIndent()

        val result = json.decodeFromString<OptionalFieldPayload>(payload)

        assert((result.label as OptionalField.Present).value == null)
    }

    @Test
    fun deserializationShouldIncludePresentField() {
        val payload = """
            {
                "label": "Test",
                "type": 123
            }
        """.trimIndent()

        val result = json.decodeFromString<OptionalFieldPayload>(payload)

        assert((result.label as OptionalField.Present).value == "Test")
        assert((result.type as OptionalField.Present).value == 123)
    }

    @Test
    fun deserializationShouldIncludeMissingField() {
        val payload = """
            {}
        """.trimIndent()

        val result = json.decodeFromString<OptionalFieldPayload>(payload)

        assert(result.label == OptionalField.Missing)
        assert(result.type == OptionalField.Missing)
    }
}
