package io.github.harryjhin.routebinding

import io.github.harryjhin.routebinding.internal.ReflectionRequestParamBinder
import io.ktor.server.application.Application
import io.ktor.server.application.createApplicationPlugin
import io.ktor.util.AttributeKey

/** Installs a request-parameter binder for typed routes. */
val RouteBinding = createApplicationPlugin(
    name = "RouteBinding",
    createConfiguration = ::RouteBindingConfig,
) {
    application.attributes.put(RequestParamBinderKey, pluginConfig.requestParamBinder ?: ReflectionRequestParamBinder)
}

@PublishedApi
internal val RequestParamBinderKey = AttributeKey<RequestParamBinder>("RouteBindingRequestParamBinder")

@PublishedApi
internal fun Application.requestParamBinderOrNull(): RequestParamBinder? =
    attributes.getOrNull(RequestParamBinderKey)
