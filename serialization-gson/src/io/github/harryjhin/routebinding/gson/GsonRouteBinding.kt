package io.github.harryjhin.routebinding.gson

import com.google.gson.Gson
import io.github.harryjhin.routebinding.RouteBindingConfig

fun RouteBindingConfig.gson(gson: Gson) {
    requestParamBinder(GsonRequestParamBinder(gson))
}
