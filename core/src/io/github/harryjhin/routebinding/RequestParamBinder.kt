package io.github.harryjhin.routebinding

import io.ktor.http.Parameters
import kotlin.reflect.KType

/**
 * Converts Ktor path and query [Parameters] into an instance of a requested Kotlin type.
 *
 * Install an implementation through [RouteBindingConfig.requestParamBinder] to use it for typed routes.
 */
fun interface RequestParamBinder {
    /** Converts [parameters] to an instance described by [type]. */
    fun bind(parameters: Parameters, type: KType): Any
}
