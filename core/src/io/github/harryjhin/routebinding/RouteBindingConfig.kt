package io.github.harryjhin.routebinding

/** Configuration for [RouteBinding]. */
class RouteBindingConfig {
    internal var requestParamBinder: RequestParamBinder? = null
        private set

    /**
     * Sets the [RequestParamBinder] that typed routes use for path and query parameters.
     *
     * Route Binding uses its built-in reflection binder when this function is not called.
     */
    fun requestParamBinder(binder: RequestParamBinder) {
        requestParamBinder = binder
    }
}
