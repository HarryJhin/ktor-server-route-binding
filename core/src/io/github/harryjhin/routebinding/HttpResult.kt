package io.github.harryjhin.routebinding

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingContext

/** A typed route handler's HTTP response. */
data class HttpResult(
    val status: HttpStatusCode,
    val body: Any? = null,
) {
    constructor(dsl: HttpResultDsl) : this(dsl.status, dsl.body)
}

@DslMarker
annotation class HttpResultDslMarker

/** Builder used by [response] to declare an HTTP status and optional body. */
@HttpResultDslMarker
class HttpResultDsl {
    var status: HttpStatusCode = HttpStatusCode.OK
        private set

    var body: Any? = null
        private set

    fun status(status: () -> HttpStatusCode) {
        this.status = status()
    }

    fun body(body: () -> Any) {
        this.body = body()
    }
}

inline fun RoutingContext.response(block: HttpResultDsl.() -> Unit): HttpResult =
    HttpResult(HttpResultDsl().apply(block))

@Suppress("NOTHING_TO_INLINE")
inline fun RoutingContext.response(): HttpResult = HttpResult(HttpStatusCode.OK)

inline fun RoutingContext.ok(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.OK, body())

@Suppress("NOTHING_TO_INLINE")
inline fun RoutingContext.ok(): HttpResult = HttpResult(HttpStatusCode.OK)

inline fun RoutingContext.created(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.Created, body())

@Suppress("NOTHING_TO_INLINE")
inline fun RoutingContext.created(): HttpResult = HttpResult(HttpStatusCode.Created)

inline fun RoutingContext.accepted(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.Accepted, body())

@Suppress("NOTHING_TO_INLINE")
inline fun RoutingContext.accepted(): HttpResult = HttpResult(HttpStatusCode.Accepted)

inline fun RoutingContext.partialContent(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.PartialContent, body())

@Suppress("NOTHING_TO_INLINE")
inline fun RoutingContext.partialContent(): HttpResult = HttpResult(HttpStatusCode.PartialContent)

@Suppress("NOTHING_TO_INLINE")
inline fun RoutingContext.noContent(): HttpResult = HttpResult(HttpStatusCode.NoContent)

inline fun RoutingContext.badRequest(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.BadRequest, body())

@Suppress("NOTHING_TO_INLINE")
inline fun RoutingContext.badRequest(): HttpResult = HttpResult(HttpStatusCode.BadRequest)

inline fun RoutingContext.unauthorized(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.Unauthorized, body())

@Suppress("NOTHING_TO_INLINE")
inline fun RoutingContext.unauthorized(): HttpResult = HttpResult(HttpStatusCode.Unauthorized)

inline fun RoutingContext.forbidden(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.Forbidden, body())

@Suppress("NOTHING_TO_INLINE")
inline fun RoutingContext.forbidden(): HttpResult = HttpResult(HttpStatusCode.Forbidden)

inline fun RoutingContext.notFound(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.NotFound, body())

@Suppress("NOTHING_TO_INLINE")
inline fun RoutingContext.notFound(): HttpResult = HttpResult(HttpStatusCode.NotFound)

inline fun RoutingContext.methodNotAllowed(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.MethodNotAllowed, body())

@Suppress("NOTHING_TO_INLINE")
inline fun RoutingContext.methodNotAllowed(): HttpResult = HttpResult(HttpStatusCode.MethodNotAllowed)

inline fun RoutingContext.conflict(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.Conflict, body())

@Suppress("NOTHING_TO_INLINE")
inline fun RoutingContext.conflict(): HttpResult = HttpResult(HttpStatusCode.Conflict)

inline fun RoutingContext.gone(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.Gone, body())

@Suppress("NOTHING_TO_INLINE")
inline fun RoutingContext.gone(): HttpResult = HttpResult(HttpStatusCode.Gone)

inline fun RoutingContext.unsupportedMediaType(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.UnsupportedMediaType, body())

@Suppress("NOTHING_TO_INLINE")
inline fun RoutingContext.unsupportedMediaType(): HttpResult = HttpResult(HttpStatusCode.UnsupportedMediaType)

inline fun RoutingContext.unprocessableEntity(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.UnprocessableEntity, body())

@Suppress("NOTHING_TO_INLINE")
inline fun RoutingContext.unprocessableEntity(): HttpResult = HttpResult(HttpStatusCode.UnprocessableEntity)

inline fun RoutingContext.tooManyRequests(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.TooManyRequests, body())

@Suppress("NOTHING_TO_INLINE")
inline fun RoutingContext.tooManyRequests(): HttpResult = HttpResult(HttpStatusCode.TooManyRequests)

inline fun RoutingContext.internalServerError(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.InternalServerError, body())

@Suppress("NOTHING_TO_INLINE")
inline fun RoutingContext.internalServerError(): HttpResult = HttpResult(HttpStatusCode.InternalServerError)

inline fun RoutingContext.notImplemented(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.NotImplemented, body())

@Suppress("NOTHING_TO_INLINE")
inline fun RoutingContext.notImplemented(): HttpResult = HttpResult(HttpStatusCode.NotImplemented)

inline fun RoutingContext.badGateway(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.BadGateway, body())

@Suppress("NOTHING_TO_INLINE")
inline fun RoutingContext.badGateway(): HttpResult = HttpResult(HttpStatusCode.BadGateway)

inline fun RoutingContext.serviceUnavailable(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.ServiceUnavailable, body())

@Suppress("NOTHING_TO_INLINE")
inline fun RoutingContext.serviceUnavailable(): HttpResult = HttpResult(HttpStatusCode.ServiceUnavailable)

inline fun RoutingContext.gatewayTimeout(body: () -> Any): HttpResult = HttpResult(HttpStatusCode.GatewayTimeout, body())

@Suppress("NOTHING_TO_INLINE")
inline fun RoutingContext.gatewayTimeout(): HttpResult = HttpResult(HttpStatusCode.GatewayTimeout)

@PublishedApi
internal suspend fun RoutingContext.respond(result: HttpResult) {
    result.body?.let { call.respond(result.status, it) } ?: call.respond(result.status)
}
