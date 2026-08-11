# Ktor Server Route Binding

[English](README.md)

[![Kotlin](https://img.shields.io/badge/kotlin-2.3.21-blue.svg?logo=kotlin)](https://kotlinlang.org)
[![Ktor Compatibility](https://github.com/HarryJhin/ktor-server-route-binding/actions/workflows/ktor-compatibility.yml/badge.svg?branch=main)](https://github.com/HarryJhin/ktor-server-route-binding/actions/workflows/ktor-compatibility.yml)
[![Ktor](https://img.shields.io/badge/Ktor-3.0%E2%80%933.5-087CFA?logo=ktor&logoColor=white)](https://github.com/HarryJhin/ktor-server-route-binding/blob/main/.github/workflows/ktor-compatibility.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.harryjhin/ktor-server-route-binding?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.harryjhin/ktor-server-route-binding)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)

[Ktor Server](https://ktor.io/)의 path·query parameter를 타입 안전한 Kotlin DTO로 바인딩합니다. Route Binding은 route handler가 타입이 지정된 request parameter를 받게 하면서, Ktor `ContentNegotiation` plugin이 request body 역직렬화와 response 직렬화를 계속 담당하게 합니다.

## 빠른 시작

core 모듈을 추가하세요. `<version>`에는 Maven Central 뱃지에 표시된 버전을 넣습니다. Ktor 버전은 Ktor BOM 한 곳에서만 결정합니다.

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("io.ktor:ktor-bom:<ktor-version>"))
    implementation("io.github.harryjhin:ktor-server-route-binding:<version>")
    implementation("io.ktor:ktor-server-content-negotiation")
}
```

> [!TIP]
> 모든 Ktor 의존성에는 버전을 쓰지 마세요. BOM이 애플리케이션 전체에서 호환되는 Ktor 버전 하나를 선택합니다.

path·query parameter에도 직렬화 정책을 적용하려면 binder 모듈을 하나만 추가하세요.

| Binder | 의존성 | `RouteBinding` 설정 |
| --- | --- | --- |
| Basic | 없음 | `install(RouteBinding)` |
| kotlinx.serialization JSON | `ktor-server-route-binding-serialization-kotlinx-json`, `ktor-serialization-kotlinx-json` | `kotlinxSerialization(json)` |
| Gson | `ktor-server-route-binding-serialization-gson`, `ktor-serialization-gson` | `gson(gson)` |
| Jackson | `ktor-server-route-binding-serialization-jackson`, `ktor-serialization-jackson` | `jackson(objectMapper)` |

## 호환성

Route Binding은 Ktor 3.x를 지원합니다. CI는 지원하는 모든 Ktor 3 minor line의 최신 patch release를 검증합니다. 애플리케이션 전체 Ktor 버전은 Ktor BOM으로 선택하세요.

Ktor JSON converter와 Route Binding에는 같은 `Json` 인스턴스를 설정하세요. 생략된 query parameter는 parameter model에 선언한 Kotlin 기본값을 사용합니다.

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
            // GET /users/42
            ok { UserResponse(params.id, params.includeDetails, role = params.role) }
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
    val role: String? = null,
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

## Route Binding이 하는 일

- Ktor path·query parameter를 Kotlin request model로 바인딩합니다.
- `RouteBinding`에 설정한 binder를 사용합니다.
- request body 처리는 Ktor `ContentNegotiation`에 맡깁니다.
- handler가 반환한 `HttpResult`를 Ktor response pipeline으로 보냅니다.

Route Binding은 Ktor routing, request validation, authentication, content negotiation을 대체하지 않습니다.

## Route Binding과 Ktor Resources

[Ktor Resources](https://ktor.io/docs/server-resources.html)와 Route Binding은 모두 path·query 값을 타입이 지정된 Kotlin 객체로 노출하지만, 모델링하는 경계가 다릅니다.

| 관심사 | Route Binding | Ktor Resources |
| --- | --- | --- |
| route 선언 | `get("/users/{id}")`처럼 기존 Ktor 문자열 DSL 유지 | 보통 중첩 class를 포함하는 `@Resource` class로 route 정의 |
| parameter 변환 | basic reflection, kotlinx.serialization JSON, Gson, Jackson 중 설치한 binder 사용 | Resources serialization model과 `@Serializable` resource class 사용 |
| request body | `post<Params, Body>`로 parameter DTO와 body DTO를 함께 수신 | resource object를 받고 `call.receive()`로 body를 별도 수신 |
| serializer 정책 | `ContentNegotiation`과 같은 serializer 인스턴스 재사용 | resource class에 kotlinx.serialization 사용 |
| reverse routing | URL을 생성하지 않음 | `href()`로 타입 안전한 URL 생성 |

route 자체가 재사용 가능한 계약이어야 하거나 중첩 resource type, 타입 안전 URL 생성, client/server 간 resource 정의 공유가 필요하면 **Ktor Resources**를 선택하세요.

기존 Ktor route를 유지하면서 반복적인 `call.parameters[...]` 추출을 DTO로 바꾸고 싶다면 **Route Binding**을 선택하세요. 특히 Gson·Jackson을 이미 사용하거나, 하나의 handler에서 타입이 지정된 path/query parameter와 request body를 함께 받아야 할 때 적합합니다.

Route Binding은 `@Resource`, 중첩 route 모델링, reverse URL 생성을 의도적으로 제공하지 않습니다. Resources API를 대체하기보다 일반 Ktor routing을 보완합니다.

> [!IMPORTANT]
> 타입 안전 URL과 재사용 가능한 route 계약이 필요하면 Resources를 선택하세요. 기존 Ktor route 모델을 바꾸지 않고 typed parameter binding을 추가하려면 Route Binding을 선택하세요.

## 선택적 query parameter

path·query parameter를 선택 사항으로 만들 때는 Kotlin 기본값을 사용하세요. 이 방식은 DTO 계약을 명확히 하고 built-in reflection binder와 모든 serialization binder에서 동작합니다.

```kotlin
@Serializable
data class UserFilter(
    val page: Int = 1,
    val size: Int = 20,
    val query: String? = null,
)
```

`GET /users`는 `page`를 `1`, `size`를 `20`, `query`를 `null`로 바인딩합니다. `GET /users?query=ktor`는 `query`만 덮어씁니다.

## Typed routing method

request body가 없는 method에는 parameter-only route를 사용하세요. body-only route에는 type parameter 하나를, parameter와 body를 함께 받는 route에는 type parameter 두 개를 사용합니다.

<details>
<summary>전체 typed HTTP method variation 보기</summary>

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

</details>

## Serialization module

`ContentNegotiation`에 등록한 converter와 일치하는 모듈을 하나만 선택하세요.

| Converter | Route Binding 모듈 | 설정 |
| --- | --- | --- |
| kotlinx.serialization JSON | `ktor-server-route-binding-serialization-kotlinx-json` | `kotlinxSerialization(json)` |
| Jackson | `ktor-server-route-binding-serialization-jackson` | `jackson(objectMapper)` |
| Gson | `ktor-server-route-binding-serialization-gson` | `gson(gson)` |

두 plugin에는 같은 `Json`, `ObjectMapper`, 또는 `Gson` 인스턴스를 전달하세요. 이렇게 하면 property name, 기본값, naming policy가 request parameter와 request body에 일관되게 적용됩니다.

모듈을 설정하지 않으면 Route Binding은 built-in reflection binder를 사용합니다. reflection binder는 Kotlin constructor parameter, scalar type, enum, value class, 반복된 `List`·`Set` parameter를 지원합니다.

## Response

route handler는 `HttpResult`를 반환합니다. 일반적인 response에는 status helper를 사용하고, 애플리케이션별 status에는 `response { ... }`를 사용하세요.

```kotlin
get<UserParams>("/users/{id}") { params ->
    findUser(params.id)?.let { user -> ok { user } } ?: notFound()
}

post<CreateUserRequest>("/users") { body ->
    created { createUser(body) }
}
```

helper는 성공 response(`ok`, `created`, `accepted`, `noContent`), client error(`badRequest`, `unauthorized`, `forbidden`, `notFound`, `conflict`), server error(`internalServerError`, `badGateway`, `serviceUnavailable`)를 지원합니다.

## Sample

각 binder에는 동일한 typed path/query binding, optional query parameter, request body 사례를 담은 실행 가능한 Ktor 애플리케이션이 있습니다. sample은 로컬 프로젝트 모듈이 아니라 Maven Central의 Route Binding release artifact를 사용합니다. 한 번에 하나만 실행하세요. 모두 `http://localhost:8080`에서 수신합니다.

| Binder | 모듈 | 실행 |
| --- | --- | --- |
| kotlinx.serialization JSON | [`samples/kotlinx-json`](samples/kotlinx-json) | `./kotlin run --module samples/kotlinx-json` |
| Gson | [`samples/gson`](samples/gson) | `./kotlin run --module samples/gson` |
| Jackson | [`samples/jackson`](samples/jackson) | `./kotlin run --module samples/jackson` |
| Basic (default reflection binder) | [`samples/basic`](samples/basic) | `./kotlin run --module samples/basic` |

`curl` 요청은 각 sample의 README에서 확인할 수 있습니다.

## Snapshot release

<details>
<summary>개발용 Snapshot 게시·사용 방법 보기</summary>

Central Portal SNAPSHOT 버전은 교체될 수 있고 repository 보존 기간 이후 만료됩니다. repository의 [snapshot publishing script](scripts/publish-snapshot.sh)는 test suite 실행 뒤 모든 모듈을 게시합니다.

Snapshot을 사용하려면 `mavenCentral()`보다 앞에 Central Portal Snapshot repository를 추가하고 dependency 좌표의 `<version>`을 Snapshot version으로 바꾸세요.

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

필수 Toolchain environment variable이 없으면 script는 `$GRADLE_USER_HOME/gradle.properties` 또는 `~/.gradle/gradle.properties`의 `mavenCentralUsername`, `mavenCentralPassword`, `signingInMemoryKey`, `signingInMemoryKeyPassword`를 읽습니다.

</details>

## License

Ktor Server Route Binding은 [Apache License, Version 2.0](LICENSE)을 따릅니다.
