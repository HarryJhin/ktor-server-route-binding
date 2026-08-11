# Default reflection binder sample

Run from the repository root:

```bash
./kotlin run --module samples-reflection
```

The server listens on `http://localhost:8080`.

```bash
curl 'http://localhost:8080/users/42'
curl 'http://localhost:8080/users/42?includeDetails=true'
curl -X POST 'http://localhost:8080/users?role=admin' \
  -H 'Content-Type: application/json' \
  -d '{"id":42,"name":"Harry"}'
```

`RouteBinding` is installed without a configured binder, so it uses the built-in reflection binder for path and query parameters. The first request omits `includeDetails` and `role`, so their Kotlin defaults (`false` and `null`) are used. `ContentNegotiation` still uses kotlinx.serialization JSON for request bodies and responses.
