package io.github.harryjhin.routebinding.jackson

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.ktor.http.ContentType
import io.github.harryjhin.routebinding.RouteBinding
import io.github.harryjhin.routebinding.badRequest
import io.github.harryjhin.routebinding.get
import io.github.harryjhin.routebinding.noContent
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import io.ktor.serialization.jackson.JacksonConverter
import kotlin.test.Test
import kotlin.test.assertEquals

class JacksonRequestParamBinderTest {
    @Test
    fun `binds json property names`() = testApplication {
        val objectMapper = ObjectMapper().registerKotlinModule()

        application {
            install(ContentNegotiation) { register(ContentType.Application.Json, JacksonConverter(objectMapper)) }
            install(RouteBinding) { jackson(objectMapper) }
            routing {
                get<JacksonParams>("/users") { requestParam ->
                    if (requestParam.sortBy == "createdAt") noContent() else badRequest()
                }
            }
        }

        assertEquals(HttpStatusCode.NoContent, client.get("/users?sort_by=createdAt").status)
    }
}

data class JacksonParams(@field:JsonProperty("sort_by") val sortBy: String)
