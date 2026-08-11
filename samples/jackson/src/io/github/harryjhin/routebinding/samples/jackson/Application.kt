package io.github.harryjhin.routebinding.samples.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.github.harryjhin.routebinding.RouteBinding
import io.github.harryjhin.routebinding.created
import io.github.harryjhin.routebinding.get
import io.github.harryjhin.routebinding.jackson.jackson
import io.github.harryjhin.routebinding.ok
import io.github.harryjhin.routebinding.post
import io.ktor.http.ContentType
import io.ktor.serialization.jackson.JacksonConverter
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing

fun main() {
    val objectMapper = ObjectMapper().registerKotlinModule()

    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            register(ContentType.Application.Json, JacksonConverter(objectMapper))
        }
        install(RouteBinding) {
            jackson(objectMapper)
        }
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

data class RegisterUserRequest(
    val id: Long,
    val name: String,
)

data class UserResponse(
    val id: Long,
    val includeDetails: Boolean,
    val name: String? = null,
    val role: String? = null,
)
