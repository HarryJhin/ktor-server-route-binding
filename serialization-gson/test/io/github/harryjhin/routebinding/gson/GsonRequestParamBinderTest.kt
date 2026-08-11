package io.github.harryjhin.routebinding.gson

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
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
import io.ktor.serialization.gson.GsonConverter
import kotlin.test.Test
import kotlin.test.assertEquals

class GsonRequestParamBinderTest {
    @Test
    fun `binds omitted nullable query parameters to their defaults`() = testApplication {
        val gson = GsonBuilder().create()

        application {
            install(RouteBinding) { gson(gson) }
            routing {
                get<OptionalGsonParams>("/users") { requestParam ->
                    if (requestParam.sortBy == null) noContent() else badRequest()
                }
            }
        }

        assertEquals(HttpStatusCode.NoContent, client.get("/users").status)
    }

    @Test
    fun `binds configured field naming policy`() = testApplication {
        val gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()

        application {
            install(ContentNegotiation) { register(ContentType.Application.Json, GsonConverter(gson)) }
            install(RouteBinding) { gson(gson) }
            routing {
                get<GsonParams>("/users") { requestParam ->
                    if (requestParam.sortBy == "createdAt") noContent() else badRequest()
                }
            }
        }

        assertEquals(HttpStatusCode.NoContent, client.get("/users?sort_by=createdAt").status)
    }
}

data class GsonParams(val sortBy: String)

data class OptionalGsonParams(val sortBy: String? = null)
