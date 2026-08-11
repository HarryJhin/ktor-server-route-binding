package io.github.harryjhin.routebinding.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.harryjhin.routebinding.RequestParamBinder
import io.ktor.http.Parameters
import kotlin.reflect.KType
import kotlin.reflect.jvm.javaType

class JacksonRequestParamBinder(
    private val objectMapper: ObjectMapper,
) : RequestParamBinder {
    override fun bind(parameters: Parameters, type: KType): Any =
        objectMapper.convertValue(parameters.toJsonNode(objectMapper), objectMapper.typeFactory.constructType(type.javaType))
}

private fun Parameters.toJsonNode(objectMapper: ObjectMapper): ObjectNode = objectMapper.createObjectNode().apply {
    entries().forEach { (name, values) ->
        if (values.size == 1) put(name, values.single())
        else putArray(name).apply { values.forEach(::add) }
    }
}
