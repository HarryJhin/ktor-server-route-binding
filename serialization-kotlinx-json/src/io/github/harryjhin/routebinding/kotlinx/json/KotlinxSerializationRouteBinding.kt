package io.github.harryjhin.routebinding.kotlinx.json

import io.github.harryjhin.routebinding.RouteBindingConfig
import kotlinx.serialization.json.Json

/** Configures Route Binding to use [json] for path and query parameter binding. */
fun RouteBindingConfig.kotlinxSerialization(json: Json = Json) {
    requestParamBinder(KotlinxSerializationRequestParamBinder(json))
}
