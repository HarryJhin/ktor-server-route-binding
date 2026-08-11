package io.github.harryjhin.routebinding.kotlinx.json

import io.github.harryjhin.routebinding.RequestParamBinder
import io.ktor.http.Parameters
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive as jsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.serializer
import kotlin.reflect.KType

class KotlinxSerializationRequestParamBinder(
    private val json: Json,
) : RequestParamBinder {
    @OptIn(ExperimentalSerializationApi::class)
    override fun bind(parameters: Parameters, type: KType): Any {
        val serializer = json.serializersModule.serializer(type)
        return requireNotNull(json.decodeFromJsonElement(serializer, parameters.toJsonElement(serializer.descriptor)))
    }
}

private fun Parameters.toJsonElement(descriptor: SerialDescriptor): JsonElement = buildJsonObject {
    repeat(descriptor.elementsCount) { index ->
        val name = descriptor.getElementName(index)
        val values = getAll(name).orEmpty()
        if (values.isNotEmpty()) put(name, values.toJsonElement(descriptor.getElementDescriptor(index)))
    }
}

private fun List<String>.toJsonElement(descriptor: SerialDescriptor): JsonElement =
    if (descriptor.kind == StructureKind.LIST) {
        JsonArray(map { it.toJsonElement(descriptor.getElementDescriptor(0)) })
    } else {
        require(size == 1) { "Parameter must have exactly one value" }
        single().toJsonElement(descriptor)
    }

private fun String.toJsonElement(descriptor: SerialDescriptor): JsonElement = when (descriptor.kind) {
    PrimitiveKind.BYTE -> jsonPrimitive(toByteOrNull() ?: error("Invalid Byte parameter: '$this'"))
    PrimitiveKind.SHORT -> jsonPrimitive(toShortOrNull() ?: error("Invalid Short parameter: '$this'"))
    PrimitiveKind.INT -> jsonPrimitive(toIntOrNull() ?: error("Invalid Int parameter: '$this'"))
    PrimitiveKind.LONG -> jsonPrimitive(toLongOrNull() ?: error("Invalid Long parameter: '$this'"))
    PrimitiveKind.FLOAT -> jsonPrimitive(toFloatOrNull() ?: error("Invalid Float parameter: '$this'"))
    PrimitiveKind.DOUBLE -> jsonPrimitive(toDoubleOrNull() ?: error("Invalid Double parameter: '$this'"))
    PrimitiveKind.BOOLEAN -> jsonPrimitive(toBooleanStrictOrNull() ?: error("Invalid Boolean parameter: '$this'"))
    else -> jsonPrimitive(this)
}
