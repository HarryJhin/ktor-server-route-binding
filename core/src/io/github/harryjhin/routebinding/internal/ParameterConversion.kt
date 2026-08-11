package io.github.harryjhin.routebinding.internal

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.primaryConstructor

internal fun convertParam(values: List<String>, type: KType): Any? {
    val classifier = type.classifier as? KClass<*> ?: error("Unsupported parameter type: $type")
    if (classifier == List::class || classifier == Set::class) {
        val elementType = type.arguments.singleOrNull()?.type
            ?: error("Collection parameter must declare an element type: $type")
        val converted = values.map { convertParam(listOf(it), elementType) }
        return if (classifier == Set::class) converted.toSet() else converted
    }
    if (classifier.isValue) {
        val constructor = classifier.primaryConstructor
            ?: error("Value class ${classifier.qualifiedName} must declare a primary constructor")
        val underlying = constructor.parameters.singleOrNull()
            ?: error("Value class ${classifier.qualifiedName} must have exactly one parameter")
        return constructor.call(convertParam(values, underlying.type))
    }
    require(values.size == 1) { "Parameter of type $type must have exactly one value" }
    val value = values.single()
    return when (classifier) {
        String::class -> value
        Int::class -> value.toIntOrNull() ?: error("Invalid Int parameter: '$value'")
        Long::class -> value.toLongOrNull() ?: error("Invalid Long parameter: '$value'")
        Short::class -> value.toShortOrNull() ?: error("Invalid Short parameter: '$value'")
        Byte::class -> value.toByteOrNull() ?: error("Invalid Byte parameter: '$value'")
        Double::class -> value.toDoubleOrNull() ?: error("Invalid Double parameter: '$value'")
        Float::class -> value.toFloatOrNull() ?: error("Invalid Float parameter: '$value'")
        Boolean::class -> value.toBooleanStrictOrNull() ?: error("Invalid Boolean parameter: '$value'")
        Char::class -> value.singleOrNull() ?: error("Invalid Char parameter: '$value'")
        else -> if (classifier.java.isEnum) {
            classifier.java.enumConstants.firstOrNull { (it as Enum<*>).name == value }
                ?: error("Invalid ${classifier.simpleName} parameter: '$value'")
        } else error("Unsupported parameter type: $type")
    }
}
