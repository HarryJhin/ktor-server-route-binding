package io.github.harryjhin.routebinding.kotlinx.json

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
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class KotlinxSerializationRequestParamBinderTest {
    @Test
    fun `binds serial names and repeated query parameters`() = testApplication {
        val json = Json

        application {
            install(ContentNegotiation) { json(json) }
            install(RouteBinding) { kotlinxSerialization(json) }
            routing {
                get<KotlinxParams>("/users") { requestParam ->
                    if (requestParam.sortBy == "createdAt" && requestParam.tags == listOf("kotlin", "ktor")) noContent()
                    else badRequest()
                }
            }
        }

        assertEquals(
            HttpStatusCode.NoContent,
            client.get("/users?sort_by=createdAt&tags=kotlin&tags=ktor").status,
        )
    }
}

@Serializable
data class KotlinxParams(
    @SerialName("sort_by") val sortBy: String,
    val tags: List<String>,
)
