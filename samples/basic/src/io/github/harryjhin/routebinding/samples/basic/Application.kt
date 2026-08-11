package io.github.harryjhin.routebinding.samples.basic

import io.github.harryjhin.routebinding.RouteBinding
import io.github.harryjhin.routebinding.created
import io.github.harryjhin.routebinding.get
import io.github.harryjhin.routebinding.ok
import io.github.harryjhin.routebinding.post
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import kotlinx.serialization.Serializable

fun main() {
    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            json()
        }
        install(RouteBinding)
        routing {
            get<UserQuery>("/users/{id}") { params ->
                ok { UserResponse(params.id, params.includeDetails, role = params.role) }
            }
            post<UserRole, RegisterUserRequest>("/users") { params, body ->
                created { UserResponse(body.id, params.includeDetails, body.name, params.role) }
            }
        }
    }.start(wait = true)
}

data class UserQuery(
    val id: Long,
    val includeDetails: Boolean = false,
    val role: String? = null,
)

data class UserRole(
    val role: String,
    val includeDetails: Boolean = false,
)

@Serializable
data class RegisterUserRequest(
    val id: Long,
    val name: String,
)

@Serializable
data class UserResponse(
    val id: Long,
    val includeDetails: Boolean,
    val name: String? = null,
    val role: String? = null,
)
