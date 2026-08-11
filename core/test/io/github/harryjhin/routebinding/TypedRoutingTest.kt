package io.github.harryjhin.routebinding

import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.head
import io.ktor.client.request.options
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.put
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals

class TypedRoutingTest {
    @Test
    fun `typed get binds omitted query parameters to their defaults`() = testApplication {
        application {
            routing {
                get<OptionalUserParams>("/users") { requestParam ->
                    if (requestParam.role == null && !requestParam.includeDetails) noContent() else badRequest()
                }
            }
        }

        assertEquals(HttpStatusCode.NoContent, client.get("/users").status)
    }

    @Test
    fun `typed post binds request parameter and request body`() = testApplication {
        application {
            install(ContentNegotiation) { json() }
            routing {
                post<UserParams, UserRegisterRequest>("/users") { requestParam, requestBody ->
                    created { UserResponse(requestParam.role, requestBody.name) }
                }
            }
        }

        val response = client.post("/users?role=admin") {
            contentType(ContentType.Application.Json)
            setBody("""{"name":"Harry"}""")
        }

        assertEquals(HttpStatusCode.Created, response.status)
        assertEquals("""{"role":"admin","name":"Harry"}""", response.bodyAsText())
    }

    @Test
    fun `typed put binds request parameter and request body`() = testApplication {
        application {
            install(ContentNegotiation) { json() }
            routing {
                put<UserParams, UserRegisterRequest>("/users") { requestParam, requestBody ->
                    ok { UserResponse(requestParam.role, requestBody.name) }
                }
            }
        }

        val response = client.put("/users?role=admin") {
            contentType(ContentType.Application.Json)
            setBody("""{"name":"Harry"}""")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("""{"role":"admin","name":"Harry"}""", response.bodyAsText())
    }

    @Test
    fun `typed put binds request body`() = testApplication {
        application {
            install(ContentNegotiation) { json() }
            routing {
                put<UserRegisterRequest>("/users") { requestBody ->
                    ok { UserResponse("editor", requestBody.name) }
                }
            }
        }

        val response = client.put("/users") {
            contentType(ContentType.Application.Json)
            setBody("""{"name":"Harry"}""")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("""{"role":"editor","name":"Harry"}""", response.bodyAsText())
    }

    @Test
    fun `typed patch binds request body`() = testApplication {
        application {
            install(ContentNegotiation) { json() }
            routing {
                patch<UserRegisterRequest>("/users") { requestBody ->
                    ok { UserResponse("editor", requestBody.name) }
                }
            }
        }

        val response = client.patch("/users") {
            contentType(ContentType.Application.Json)
            setBody("""{"name":"Harry"}""")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("""{"role":"editor","name":"Harry"}""", response.bodyAsText())
    }

    @Test
    fun `typed patch binds request parameter and request body`() = testApplication {
        application {
            install(ContentNegotiation) { json() }
            routing {
                patch<UserParams, UserRegisterRequest>("/users") { requestParam, requestBody ->
                    ok { UserResponse(requestParam.role, requestBody.name) }
                }
            }
        }

        val response = client.patch("/users?role=editor") {
            contentType(ContentType.Application.Json)
            setBody("""{"name":"Harry"}""")
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("""{"role":"editor","name":"Harry"}""", response.bodyAsText())
    }

    @Test
    fun `typed delete binds request parameter`() = testApplication {
        application {
            routing {
                delete<UserParams>("/users") { requestParam ->
                    noContent()
                }
            }
        }

        val response = client.delete("/users?role=admin")

        assertEquals(HttpStatusCode.NoContent, response.status)
    }

    @Test
    fun `typed head binds request parameter`() = testApplication {
        application {
            routing {
                head<UserParams>("/users") { requestParam ->
                    ok()
                }
            }
        }

        val response = client.head("/users?role=admin")

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `typed options binds request parameter`() = testApplication {
        application {
            routing {
                options<UserParams>("/users") { requestParam ->
                    ok()
                }
            }
        }

        val response = client.options("/users?role=admin")

        assertEquals(HttpStatusCode.OK, response.status)
    }
}

data class UserParams(val role: String)

data class OptionalUserParams(
    val role: String? = null,
    val includeDetails: Boolean = false,
)

@Serializable
data class UserRegisterRequest(val name: String)

@Serializable
data class UserResponse(val role: String, val name: String)
