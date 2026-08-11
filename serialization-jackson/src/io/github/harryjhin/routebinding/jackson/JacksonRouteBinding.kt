package io.github.harryjhin.routebinding.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.harryjhin.routebinding.RouteBindingConfig

fun RouteBindingConfig.jackson(objectMapper: ObjectMapper) {
    requestParamBinder(JacksonRequestParamBinder(objectMapper))
}
