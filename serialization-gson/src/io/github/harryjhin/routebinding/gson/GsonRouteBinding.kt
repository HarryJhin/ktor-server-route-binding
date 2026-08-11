package io.github.harryjhin.routebinding.gson

import com.google.gson.Gson
import io.github.harryjhin.routebinding.RouteBindingConfig

/** Configures Route Binding to use [gson] for path and query parameter binding. */
fun RouteBindingConfig.gson(gson: Gson) {
    requestParamBinder(GsonRequestParamBinder(gson))
}
