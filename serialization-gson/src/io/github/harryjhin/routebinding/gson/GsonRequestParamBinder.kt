package io.github.harryjhin.routebinding.gson

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import io.github.harryjhin.routebinding.RequestParamBinder
import io.ktor.http.Parameters
import kotlin.ExperimentalStdlibApi
import kotlin.reflect.KType
import kotlin.reflect.jvm.javaType

class GsonRequestParamBinder(
    private val gson: Gson,
) : RequestParamBinder {
    @OptIn(ExperimentalStdlibApi::class)
    override fun bind(parameters: Parameters, type: KType): Any =
        gson.fromJson(parameters.toJsonObject(), type.javaType)
}

private fun Parameters.toJsonObject(): JsonObject = JsonObject().apply {
    entries().forEach { (name, values) ->
        if (values.size == 1) add(name, JsonPrimitive(values.single()))
        else add(name, com.google.gson.JsonArray().apply { values.forEach { add(it) } })
    }
}
