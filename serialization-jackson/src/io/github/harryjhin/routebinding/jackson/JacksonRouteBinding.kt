package io.github.harryjhin.routebinding.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.harryjhin.routebinding.RouteBindingConfig

/** Configures Route Binding to use [objectMapper] for path and query parameter binding. */
fun RouteBindingConfig.jackson(objectMapper: ObjectMapper) {
    requestParamBinder(JacksonRequestParamBinder(objectMapper))
}
