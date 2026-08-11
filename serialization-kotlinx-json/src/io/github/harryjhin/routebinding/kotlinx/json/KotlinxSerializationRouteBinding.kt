package io.github.harryjhin.routebinding.kotlinx.json

import io.github.harryjhin.routebinding.RouteBindingConfig
import kotlinx.serialization.json.Json

fun RouteBindingConfig.kotlinxSerialization(json: Json = Json) {
    requestParamBinder(KotlinxSerializationRequestParamBinder(json))
}
