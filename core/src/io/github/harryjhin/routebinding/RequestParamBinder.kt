package io.github.harryjhin.routebinding

import io.ktor.http.Parameters
import kotlin.reflect.KType

/** Converts path and query parameters into a requested Kotlin type. */
fun interface RequestParamBinder {
    fun bind(parameters: Parameters, type: KType): Any
}
