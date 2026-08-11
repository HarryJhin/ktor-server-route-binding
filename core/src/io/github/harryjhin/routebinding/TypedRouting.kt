package io.github.harryjhin.routebinding

import io.github.harryjhin.routebinding.internal.ReflectionRequestParamBinder
import io.ktor.http.Parameters
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingContext
import io.ktor.server.routing.delete as ktorDelete
import io.ktor.server.routing.get as ktorGet
import io.ktor.server.routing.head as ktorHead
import io.ktor.server.routing.options as ktorOptions
import io.ktor.server.routing.patch as ktorPatch
import io.ktor.server.routing.post as ktorPost
import io.ktor.server.routing.put as ktorPut
import kotlin.reflect.KType
import kotlin.reflect.typeOf

/**
 * Registers a typed GET route.
 *
 * The route binds path and query parameters to [P] with the binder installed by [RouteBinding],
 * invokes [body], and sends the returned [HttpResult].
 *
 * @param path Ktor route path pattern.
 * @param body Handler that receives the bound request parameters.
 * @return The registered route.
 */
inline fun <reified P : Any> Route.get(
    path: String,
    crossinline body: suspend RoutingContext.(requestParam: P) -> HttpResult,
): Route = ktorGet(path) {
    val requestParam = bindParams(call.parameters, typeOf<P>(), call.application.requestParamBinderOrNull()) as P
    respond(body(requestParam))
}

/**
 * Registers a typed POST route with request parameters and a request body.
 *
 * The route binds path and query parameters to [P] with the binder installed by [RouteBinding].
 * It deserializes the request body as [B] through the application's `ContentNegotiation`
 * configuration, invokes [body], and sends the returned [HttpResult].
 *
 * @param path Ktor route path pattern.
 * @param body Handler that receives the bound request parameters and deserialized request body.
 * @return The registered route.
 */
inline fun <reified P : Any, reified B : Any> Route.post(
    path: String,
    crossinline body: suspend RoutingContext.(requestParam: P, requestBody: B) -> HttpResult,
): Route = ktorPost(path) {
    val requestParam = bindParams(call.parameters, typeOf<P>(), call.application.requestParamBinderOrNull()) as P
    respond(body(requestParam, call.receive<B>()))
}

/**
 * Registers a typed POST route with a request body.
 *
 * The route deserializes the request body as [B] through the application's `ContentNegotiation`
 * configuration, invokes [body], and sends the returned [HttpResult].
 *
 * @param path Ktor route path pattern.
 * @param body Handler that receives the deserialized request body.
 * @return The registered route.
 */
inline fun <reified B : Any> Route.post(
    path: String,
    crossinline body: suspend RoutingContext.(requestBody: B) -> HttpResult,
): Route = ktorPost(path) {
    respond(body(call.receive<B>()))
}

/**
 * Registers a typed PUT route with request parameters and a request body.
 *
 * The route binds path and query parameters to [P] with the binder installed by [RouteBinding].
 * It deserializes the request body as [B] through the application's `ContentNegotiation`
 * configuration, invokes [body], and sends the returned [HttpResult].
 *
 * @param path Ktor route path pattern.
 * @param body Handler that receives the bound request parameters and deserialized request body.
 * @return The registered route.
 */
inline fun <reified P : Any, reified B : Any> Route.put(
    path: String,
    crossinline body: suspend RoutingContext.(requestParam: P, requestBody: B) -> HttpResult,
): Route = ktorPut(path) {
    val requestParam = bindParams(call.parameters, typeOf<P>(), call.application.requestParamBinderOrNull()) as P
    respond(body(requestParam, call.receive<B>()))
}

/**
 * Registers a typed PUT route with a request body.
 *
 * The route deserializes the request body as [B] through the application's `ContentNegotiation`
 * configuration, invokes [body], and sends the returned [HttpResult].
 *
 * @param path Ktor route path pattern.
 * @param body Handler that receives the deserialized request body.
 * @return The registered route.
 */
inline fun <reified B : Any> Route.put(
    path: String,
    crossinline body: suspend RoutingContext.(requestBody: B) -> HttpResult,
): Route = ktorPut(path) {
    respond(body(call.receive<B>()))
}

/**
 * Registers a typed PATCH route with request parameters and a request body.
 *
 * The route binds path and query parameters to [P] with the binder installed by [RouteBinding].
 * It deserializes the request body as [B] through the application's `ContentNegotiation`
 * configuration, invokes [body], and sends the returned [HttpResult].
 *
 * @param path Ktor route path pattern.
 * @param body Handler that receives the bound request parameters and deserialized request body.
 * @return The registered route.
 */
inline fun <reified P : Any, reified B : Any> Route.patch(
    path: String,
    crossinline body: suspend RoutingContext.(requestParam: P, requestBody: B) -> HttpResult,
): Route = ktorPatch(path) {
    val requestParam = bindParams(call.parameters, typeOf<P>(), call.application.requestParamBinderOrNull()) as P
    respond(body(requestParam, call.receive<B>()))
}

/**
 * Registers a typed PATCH route with a request body.
 *
 * The route deserializes the request body as [B] through the application's `ContentNegotiation`
 * configuration, invokes [body], and sends the returned [HttpResult].
 *
 * @param path Ktor route path pattern.
 * @param body Handler that receives the deserialized request body.
 * @return The registered route.
 */
inline fun <reified B : Any> Route.patch(
    path: String,
    crossinline body: suspend RoutingContext.(requestBody: B) -> HttpResult,
): Route = ktorPatch(path) {
    respond(body(call.receive<B>()))
}

/**
 * Registers a typed DELETE route.
 *
 * The route binds path and query parameters to [P] with the binder installed by [RouteBinding],
 * invokes [body], and sends the returned [HttpResult].
 *
 * @param path Ktor route path pattern.
 * @param body Handler that receives the bound request parameters.
 * @return The registered route.
 */
inline fun <reified P : Any> Route.delete(
    path: String,
    crossinline body: suspend RoutingContext.(requestParam: P) -> HttpResult,
): Route = ktorDelete(path) {
    val requestParam = bindParams(call.parameters, typeOf<P>(), call.application.requestParamBinderOrNull()) as P
    respond(body(requestParam))
}

/**
 * Registers a typed HEAD route.
 *
 * The route binds path and query parameters to [P] with the binder installed by [RouteBinding],
 * invokes [body], and sends the returned [HttpResult].
 *
 * @param path Ktor route path pattern.
 * @param body Handler that receives the bound request parameters.
 * @return The registered route.
 */
inline fun <reified P : Any> Route.head(
    path: String,
    crossinline body: suspend RoutingContext.(requestParam: P) -> HttpResult,
): Route = ktorHead(path) {
    val requestParam = bindParams(call.parameters, typeOf<P>(), call.application.requestParamBinderOrNull()) as P
    respond(body(requestParam))
}

/**
 * Registers a typed OPTIONS route.
 *
 * The route binds path and query parameters to [P] with the binder installed by [RouteBinding],
 * invokes [body], and sends the returned [HttpResult].
 *
 * @param path Ktor route path pattern.
 * @param body Handler that receives the bound request parameters.
 * @return The registered route.
 */
inline fun <reified P : Any> Route.options(
    path: String,
    crossinline body: suspend RoutingContext.(requestParam: P) -> HttpResult,
): Route = ktorOptions(path) {
    val requestParam = bindParams(call.parameters, typeOf<P>(), call.application.requestParamBinderOrNull()) as P
    respond(body(requestParam))
}

@PublishedApi
internal fun bindParams(
    parameters: Parameters,
    type: KType,
    binder: RequestParamBinder?,
): Any = (binder ?: ReflectionRequestParamBinder).bind(parameters, type)
