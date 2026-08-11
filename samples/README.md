# Samples

Runnable Ktor Server Route Binding examples. This module is not published and resolves Route Binding from the Central Portal SNAPSHOT repository instead of local project modules.

Run the server from the repository root:

```bash
./kotlin run --module samples
```

The server listens on `http://localhost:8080`.

```bash
curl 'http://localhost:8080/users/42'
curl 'http://localhost:8080/users/42?includeDetails=true'
curl -X POST 'http://localhost:8080/users?role=admin' \
  -H 'Content-Type: application/json' \
  -d '{"id":42,"name":"Harry"}'
```

The first request omits both optional query parameters. `UserQuery.includeDetails` uses its `false` default and `UserQuery.role` uses its `null` default.

The sample deliberately installs `ContentNegotiation` and `RouteBinding` with the same `Json` instance. `ContentNegotiation` decodes request bodies and encodes responses; RouteBinding maps path and query parameters to typed request models.
