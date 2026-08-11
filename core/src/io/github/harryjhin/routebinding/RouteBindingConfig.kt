package io.github.harryjhin.routebinding

/** Configures the request-parameter binder used by [RouteBinding]. */
class RouteBindingConfig {
    internal var requestParamBinder: RequestParamBinder? = null
        private set

    fun requestParamBinder(binder: RequestParamBinder) {
        requestParamBinder = binder
    }
}
