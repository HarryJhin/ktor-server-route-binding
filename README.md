# Ktor Server Route Binding

[![Kotlin](https://img.shields.io/badge/kotlin-2.3.21-blue.svg?logo=kotlin)](https://kotlinlang.org)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.harryjhin/ktor-server-route-binding?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.harryjhin/ktor-server-route-binding)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)

Typed path and query parameter binding for [Ktor Server](https://ktor.io/). Route Binding lets route handlers receive typed request parameters while Ktor's `ContentNegotiation` plugin continues to deserialize request bodies and serialize responses.

## Quick start

Add the core module and one binder that matches the serialization converter used by your application. Replace `<version>` with the version shown in the Maven Central badge.

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    val ktorVersion = "3.5.1"

    implementation(platform("io.ktor:ktor-bom:$ktorVersion"))
    implementation("io.github.harryjhin:ktor-server-route-binding:<version>")
    implementation("io.github.harryjhin:ktor-server-route-binding-serialization-kotlinx-json:<version>")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
}
```

## Compatibility

Route Binding supports Ktor 3.x. CI verifies the latest patch release in every supported Ktor 3 minor line. Use the Ktor BOM to select one Ktor version for the entire application.

Configure Ktor's JSON converter and Route Binding with the same `Json` instance. Then declare typed routes.

```kotlin
import io.github.harryjhin.routebinding.RouteBinding
import io.github.harryjhin.routebinding.created
import io.github.harryjhin.routebinding.get
import io.github.harryjhin.routebinding.kotlinx.json.kotlinxSerialization
import io.github.harryjhin.routebinding.ok
import io.github.harryjhin.routebinding.post
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

fun Application.module() {
    val json = Json { ignoreUnknownKeys = true }

    install(ContentNegotiation) {
        json(json)
    }
    install(RouteBinding) {
        kotlinxSerialization(json)
    }
    routing {
        get<UserParams>("/users/{id}") { params ->
            // GET /users/42?includeDetails=true
            ok { UserResponse(params.id, params.includeDetails) }
        }
        post<UserRole, RegisterUserRequest>("/users") { params, body ->
            // POST /users?role=admin
            created { UserResponse(body.id, role = params.role, name = body.name) }
        }
    }
}

@Serializable
data class UserParams(
    val id: Long,
    val includeDetails: Boolean = false,
)

@Serializable
data class UserRole(val role: String)

@Serializable
data class RegisterUserRequest(val id: Long, val name: String)

@Serializable
data class UserResponse(
    val id: Long,
    val includeDetails: Boolean = false,
    val role: String? = null,
    val name: String? = null,
)
```

## What Route Binding does

- Binds Ktor path and query parameters to a Kotlin request model.
- Uses a binder configured through `RouteBinding`.
- Passes the request body to Ktor `ContentNegotiation`.
- Sends the `HttpResult` returned by the handler through Ktor's response pipeline.

Route Binding does not replace Ktor routing, request validation, authentication, or content negotiation.

## Typed routing methods

Use parameter-only routes for methods without a request body. Use one type parameter for a body-only route, or two type parameters for a route with both parameters and a body.

```kotlin
routing {
    get<UserParams>("/users/{id}") { params -> ok { findUser(params.id) } }
    head<UserParams>("/users/{id}") { params -> ok() }
    options<UserParams>("/users/{id}") { params -> ok() }
    delete<UserParams>("/users/{id}") { params -> noContent() }

    post<CreateUserRequest>("/users") { body -> created { createUser(body) } }
    post<UserRole, CreateUserRequest>("/users") { params, body -> created { createUser(params, body) } }

    put<ReplaceUserRequest>("/users/{id}") { body -> ok { replaceUser(body) } }
    put<UserParams, ReplaceUserRequest>("/users/{id}") { params, body -> ok { replaceUser(params.id, body) } }

    patch<UpdateUserRequest>("/users/{id}") { body -> ok { updateUser(body) } }
    patch<UserParams, UpdateUserRequest>("/users/{id}") { params, body -> ok { updateUser(params.id, body) } }
}
```

## Serialization modules

Choose exactly one module that matches the converter registered in `ContentNegotiation`.

| Converter | Route Binding module | Configuration |
| --- | --- | --- |
| kotlinx.serialization JSON | `ktor-server-route-binding-serialization-kotlinx-json` | `kotlinxSerialization(json)` |
| Jackson | `ktor-server-route-binding-serialization-jackson` | `jackson(objectMapper)` |
| Gson | `ktor-server-route-binding-serialization-gson` | `gson(gson)` |

Pass the same `Json`, `ObjectMapper`, or `Gson` instance to both plugins. That keeps property names, defaults, and naming policies consistent between request parameters and request bodies.

## Samples

Each binder has a runnable Ktor application with the same typed path/query binding, optional query parameter, and request-body handling cases. The samples resolve Route Binding from the Central Portal SNAPSHOT repository, not local project modules. Run one sample at a time; each listens on `http://localhost:8080`.

| Binder | Module | Run |
| --- | --- | --- |
| kotlinx.serialization JSON | [`samples`](samples) | `./kotlin run --module samples` |
| Gson | [`samples-gson`](samples-gson) | `./kotlin run --module samples-gson` |
| Jackson | [`samples-jackson`](samples-jackson) | `./kotlin run --module samples-jackson` |
| Default reflection binder | [`samples-reflection`](samples-reflection) | `./kotlin run --module samples-reflection` |

See each sample's README for `curl` requests.

## Snapshot releases

Central Portal SNAPSHOT versions may be replaced and expire after the repository retention period. The repository's [snapshot publishing script](scripts/publish-snapshot.sh) publishes every module after running the test suite.

To consume a Snapshot, add the Central Portal Snapshot repository before `mavenCentral()` and replace `<version>` in the dependency coordinates with the Snapshot version.

```kotlin
repositories {
    maven {
        url = uri("https://central.sonatype.com/repository/maven-snapshots/")
        content { includeGroup("io.github.harryjhin") }
    }
    mavenCentral()
}
```

```bash
./scripts/publish-snapshot.sh 0.0.2-SNAPSHOT
```

When the required Toolchain environment variables are absent, the script reads the existing Gradle properties `mavenCentralUsername`, `mavenCentralPassword`, `signingInMemoryKey`, and `signingInMemoryKeyPassword` from `$GRADLE_USER_HOME/gradle.properties` or `~/.gradle/gradle.properties`.

## License

Ktor Server Route Binding is licensed under the [Apache License, Version 2.0](LICENSE).
