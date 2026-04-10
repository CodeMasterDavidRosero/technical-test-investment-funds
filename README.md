# Fund Management Service (technical test)

> This repository does **not** correspond to official BTG Pactual code; it is a personal technical test. Keep the repo private and use generic resource names only.

## Quick start
- Java 21, Maven 3.9+, Spring Boot 3.5.x
- MongoDB URI: `MONGODB_URI` (default `mongodb://localhost:27017/test_fund_db`)
- Postgres: `POSTGRES_URL`, `POSTGRES_USER`, `POSTGRES_PASSWORD` (default `jdbc:postgresql://localhost:5432/test_sql_db`, `app_user`, `app_password`)

## Guidelines to avoid bank references
- Do not include the bank name in package, repo, database, or resource names.
- Use neutral examples (e.g., `test-db`, `sample-key`); avoid simulated names with the bank.
- If a public repo is required by a third party, keep naming generic and include the disclaimer above.

## Next steps (high level)
1) Define domain model for funds, clients, and transactions (Mongo-first).
2) Implement REST API for subscribe/cancel/history with validations and notifications stubs. ✅ basic version added.
3) Add auth (JWT + roles), logging, and tests. ⏩
4) Provide IaC (Terraform or Serverless) and Postman collection. ⏩

## API sketch (v1)
- `GET /api/v1/clients` -> list clients (default seeded)
- `GET /api/v1/funds` -> list funds
- `POST /api/v1/subscriptions` -> subscribe `{clientId, fundCode, amount, notificationChannel?}`
- `DELETE /api/v1/subscriptions/{id}?clientId=` -> cancel and refund
- `GET /api/v1/subscriptions/transactions?clientId=` -> history

## Swagger / OpenAPI
- UI: `http://localhost:8080/swagger-ui.html`
- Docs: `http://localhost:8080/v3/api-docs`

## Local infra (Mongo + Postgres)
- Requisitos: Docker Desktop.
- Levantar bases:
  ```
  docker-compose up -d
  ```
- Por defecto la app solo necesita Mongo (datasource SQL quedó fuera de `application.yaml`).
- Usa el perfil `local` cuando quieras habilitar Postgres:
  ```
  # PowerShell
  $env:SPRING_PROFILES_ACTIVE="local"
  .\mvnw spring-boot:run
  ```
  (o `mvn spring-boot:run -Dspring-boot.run.profiles=local`)

## Cloud profile (Railway)
- Crea un `.env` (ya git-ignorado) con tus credenciales:
  - `MONGODB_URI=<tu-uri-atlas>`
  - `POSTGRES_URL=postgresql://...` (Railway)
  - `POSTGRES_USER`, `POSTGRES_PASSWORD`
- Ejecuta con el perfil `cloud`:
  ```
  $env:SPRING_PROFILES_ACTIVE="cloud"
  .\mvnw spring-boot:run
  ```

### Arranque rápido cargando .env (Windows/PowerShell)
```
.\scripts\run-with-env.ps1 -Profile cloud -SkipTests
```
El script lee `.env`, exporta las variables, usa repo local en `.m2\repository` y lanza `spring-boot:run`. Cambia `-Profile` a `local` si quieres apuntar a Postgres local.

### Si tu red bloquea el puerto 5432
- Railway expone el Postgres en `postgres-production-xxx.up.railway.app:5432`. Si no tienes salida TCP 5432, el pool fallará.
- Mientras habilitas el puerto / usas VPN, arranca sólo con Mongo usando el perfil `cloud-nosql`:
  ```
  .\scripts\run-with-env.ps1 -Profile cloud-nosql -SkipTests
  ```
  (usa sólo `MONGODB_URI` y excluye la auto-configuración JDBC/JPA).
