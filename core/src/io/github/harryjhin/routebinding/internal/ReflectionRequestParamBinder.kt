package io.github.harryjhin.routebinding.internal

import io.github.harryjhin.routebinding.RequestParamBinder
import io.ktor.http.Parameters
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.primaryConstructor

internal object ReflectionRequestParamBinder : RequestParamBinder {
    override fun bind(parameters: Parameters, type: KType): Any {
        val classifier = type.classifier as? KClass<*>
            ?: error("Unsupported parameter type: $type")
        val constructor = classifier.primaryConstructor
            ?: error("${classifier.qualifiedName} must declare a primary constructor")
        val arguments = buildMap<KParameter, Any?> {
            constructor.parameters.forEach { parameter ->
                val name = parameter.name ?: error("All parameters of ${classifier.qualifiedName} must be named")
                val values = parameters.getAll(name).orEmpty()
                if (values.isEmpty()) {
                    if (!parameter.isOptional && !parameter.type.isMarkedNullable) error("Missing required parameter '$name'")
                    return@forEach
                }
                put(parameter, convertParam(values, parameter.type))
            }
        }
        return constructor.callBy(arguments)
    }
}
