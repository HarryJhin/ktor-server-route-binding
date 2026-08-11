package io.github.harryjhin.routebinding

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingContext

/**
 * HTTP response returned from a typed route handler.
 *
 * Ktor writes [status] and serializes [body], when present, through its response pipeline.
 */
data class HttpResult(
    val status: HttpStatusCode,
    val body: Any? = null,
) {
    constructor(dsl: HttpResultDsl) : this(dsl.status, dsl.body)
}

@DslMarker
/** Marks the [HttpResultDsl] scope. */
annotation class HttpResultDslMarker

/** Builds an [HttpResult] for [response]. */
@HttpResultDslMarker
class HttpResultDsl {
    /** Status returned by the route. Defaults to [HttpStatusCode.OK]. */
    var status: HttpStatusCode = HttpStatusCode.OK
        private set

    /** Body returned by the route. Defaults to `null`. */
    var body: Any? = null
        private set

    /** Sets the response status from [status]. */
    fun status(status: () -> HttpStatusCode) {
        this.status = status()
    }

    /** Sets the response body from [body]. */
    fun body(body: () -> Any) {
        this.body = body()
    }
}

/** Builds an [HttpResult] by applying [block]. */
inline fun RoutingContext.response(block: HttpResultDsl.() -> Unit): HttpResult =
    HttpResult(HttpResultDsl().apply(block))

@Suppress("NOTHING_TO_INLINE")
/** Returns an empty [HttpStatusCode.OK] response. */
inline fun RoutingContext.response(): HttpResult = HttpResult(HttpStatusCode.OK)

/** Returns an [HttpStatusCode.OK] response with a body from [body]. */
inline fun RoutingContext.ok(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.OK, body())

@Suppress("NOTHING_TO_INLINE")
/** Returns an empty [HttpStatusCode.OK] response. */
inline fun RoutingContext.ok(): HttpResult = HttpResult(HttpStatusCode.OK)

/** Returns an [HttpStatusCode.Created] response with a body from [body]. */
inline fun RoutingContext.created(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.Created, body())

@Suppress("NOTHING_TO_INLINE")
/** Returns an empty [HttpStatusCode.Created] response. */
inline fun RoutingContext.created(): HttpResult = HttpResult(HttpStatusCode.Created)

/** Returns an [HttpStatusCode.Accepted] response with a body from [body]. */
inline fun RoutingContext.accepted(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.Accepted, body())

@Suppress("NOTHING_TO_INLINE")
/** Returns an empty [HttpStatusCode.Accepted] response. */
inline fun RoutingContext.accepted(): HttpResult = HttpResult(HttpStatusCode.Accepted)

/** Returns an [HttpStatusCode.PartialContent] response with a body from [body]. */
inline fun RoutingContext.partialContent(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.PartialContent, body())

@Suppress("NOTHING_TO_INLINE")
/** Returns an empty [HttpStatusCode.PartialContent] response. */
inline fun RoutingContext.partialContent(): HttpResult = HttpResult(HttpStatusCode.PartialContent)

@Suppress("NOTHING_TO_INLINE")
/** Returns an empty [HttpStatusCode.NoContent] response. */
inline fun RoutingContext.noContent(): HttpResult = HttpResult(HttpStatusCode.NoContent)

/** Returns an [HttpStatusCode.BadRequest] response with a body from [body]. */
inline fun RoutingContext.badRequest(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.BadRequest, body())

@Suppress("NOTHING_TO_INLINE")
/** Returns an empty [HttpStatusCode.BadRequest] response. */
inline fun RoutingContext.badRequest(): HttpResult = HttpResult(HttpStatusCode.BadRequest)

/** Returns an [HttpStatusCode.Unauthorized] response with a body from [body]. */
inline fun RoutingContext.unauthorized(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.Unauthorized, body())

@Suppress("NOTHING_TO_INLINE")
/** Returns an empty [HttpStatusCode.Unauthorized] response. */
inline fun RoutingContext.unauthorized(): HttpResult = HttpResult(HttpStatusCode.Unauthorized)

/** Returns an [HttpStatusCode.Forbidden] response with a body from [body]. */
inline fun RoutingContext.forbidden(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.Forbidden, body())

@Suppress("NOTHING_TO_INLINE")
/** Returns an empty [HttpStatusCode.Forbidden] response. */
inline fun RoutingContext.forbidden(): HttpResult = HttpResult(HttpStatusCode.Forbidden)

/** Returns an [HttpStatusCode.NotFound] response with a body from [body]. */
inline fun RoutingContext.notFound(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.NotFound, body())

@Suppress("NOTHING_TO_INLINE")
/** Returns an empty [HttpStatusCode.NotFound] response. */
inline fun RoutingContext.notFound(): HttpResult = HttpResult(HttpStatusCode.NotFound)

/** Returns an [HttpStatusCode.MethodNotAllowed] response with a body from [body]. */
inline fun RoutingContext.methodNotAllowed(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.MethodNotAllowed, body())

@Suppress("NOTHING_TO_INLINE")
/** Returns an empty [HttpStatusCode.MethodNotAllowed] response. */
inline fun RoutingContext.methodNotAllowed(): HttpResult = HttpResult(HttpStatusCode.MethodNotAllowed)

/** Returns an [HttpStatusCode.Conflict] response with a body from [body]. */
inline fun RoutingContext.conflict(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.Conflict, body())

@Suppress("NOTHING_TO_INLINE")
/** Returns an empty [HttpStatusCode.Conflict] response. */
inline fun RoutingContext.conflict(): HttpResult = HttpResult(HttpStatusCode.Conflict)

/** Returns an [HttpStatusCode.Gone] response with a body from [body]. */
inline fun RoutingContext.gone(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.Gone, body())

@Suppress("NOTHING_TO_INLINE")
/** Returns an empty [HttpStatusCode.Gone] response. */
inline fun RoutingContext.gone(): HttpResult = HttpResult(HttpStatusCode.Gone)

/** Returns an [HttpStatusCode.UnsupportedMediaType] response with a body from [body]. */
inline fun RoutingContext.unsupportedMediaType(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.UnsupportedMediaType, body())

@Suppress("NOTHING_TO_INLINE")
/** Returns an empty [HttpStatusCode.UnsupportedMediaType] response. */
inline fun RoutingContext.unsupportedMediaType(): HttpResult = HttpResult(HttpStatusCode.UnsupportedMediaType)

/** Returns an [HttpStatusCode.UnprocessableEntity] response with a body from [body]. */
inline fun RoutingContext.unprocessableEntity(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.UnprocessableEntity, body())

@Suppress("NOTHING_TO_INLINE")
/** Returns an empty [HttpStatusCode.UnprocessableEntity] response. */
inline fun RoutingContext.unprocessableEntity(): HttpResult = HttpResult(HttpStatusCode.UnprocessableEntity)

/** Returns an [HttpStatusCode.TooManyRequests] response with a body from [body]. */
inline fun RoutingContext.tooManyRequests(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.TooManyRequests, body())

@Suppress("NOTHING_TO_INLINE")
/** Returns an empty [HttpStatusCode.TooManyRequests] response. */
inline fun RoutingContext.tooManyRequests(): HttpResult = HttpResult(HttpStatusCode.TooManyRequests)

/** Returns an [HttpStatusCode.InternalServerError] response with a body from [body]. */
inline fun RoutingContext.internalServerError(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.InternalServerError, body())

@Suppress("NOTHING_TO_INLINE")
/** Returns an empty [HttpStatusCode.InternalServerError] response. */
inline fun RoutingContext.internalServerError(): HttpResult = HttpResult(HttpStatusCode.InternalServerError)

/** Returns an [HttpStatusCode.NotImplemented] response with a body from [body]. */
inline fun RoutingContext.notImplemented(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.NotImplemented, body())

@Suppress("NOTHING_TO_INLINE")
/** Returns an empty [HttpStatusCode.NotImplemented] response. */
inline fun RoutingContext.notImplemented(): HttpResult = HttpResult(HttpStatusCode.NotImplemented)

/** Returns an [HttpStatusCode.BadGateway] response with a body from [body]. */
inline fun RoutingContext.badGateway(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.BadGateway, body())

@Suppress("NOTHING_TO_INLINE")
/** Returns an empty [HttpStatusCode.BadGateway] response. */
inline fun RoutingContext.badGateway(): HttpResult = HttpResult(HttpStatusCode.BadGateway)

/** Returns an [HttpStatusCode.ServiceUnavailable] response with a body from [body]. */
inline fun RoutingContext.serviceUnavailable(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.ServiceUnavailable, body())

@Suppress("NOTHING_TO_INLINE")
/** Returns an empty [HttpStatusCode.ServiceUnavailable] response. */
inline fun RoutingContext.serviceUnavailable(): HttpResult = HttpResult(HttpStatusCode.ServiceUnavailable)

/** Returns an [HttpStatusCode.GatewayTimeout] response with a body from [body]. */
inline fun RoutingContext.gatewayTimeout(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.GatewayTimeout, body())

@Suppress("NOTHING_TO_INLINE")
/** Returns an empty [HttpStatusCode.GatewayTimeout] response. */
inline fun RoutingContext.gatewayTimeout(): HttpResult = HttpResult(HttpStatusCode.GatewayTimeout)

@PublishedApi
internal suspend fun RoutingContext.respond(result: HttpResult) {
    result.body?.let { call.respond(result.status, it) } ?: call.respond(result.status)
}
