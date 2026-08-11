# Gson sample

Run from the repository root:

```bash
./kotlin run --module samples/gson
```

The server listens on `http://localhost:8080`.

```bash
curl 'http://localhost:8080/users/42'
curl 'http://localhost:8080/users/42?includeDetails=true'
curl -X POST 'http://localhost:8080/users?role=admin' \
  -H 'Content-Type: application/json' \
  -d '{"id":42,"name":"Harry"}'
```

The first request omits `includeDetails` and `role`, so their Kotlin defaults (`false` and `null`) are used. The sample configures Ktor `ContentNegotiation` and Route Binding with the same `Gson` instance.
