# Fund Management Service (technical test)

> This repository does **not** correspond to official BTG Pactual code; it is a personal technical test. Keep the repo private and use generic resource names only.

## Quick start
- Java 21, Maven 3.9+, Spring Boot 3.5.x
- MongoDB URI: `MONGODB_URI` (por defecto `mongodb://localhost:27017/test_fund_db`)

## Guidelines to avoid bank references
- No incluyas el nombre del banco en paquetes, repos, bases o recursos.
- Usa ejemplos neutros (ej. `test-db`, `sample-key`); evita nombres simulados del banco.
- Si necesitas repo público, mantén nombres genéricos e incluye el disclaimer anterior.

## Next steps (high level)
1) Definir dominio para fondos, clientes y transacciones (Mongo-first).
2) Implementar REST para subscribe/cancel/history con validaciones y notificaciones stub. ✅ básico listo.
3) Añadir auth (JWT + roles), logging y tests. ⏳
4) Proveer IaC y colección Postman. ⏳

## API sketch (v1)
- `GET /api/v1/clients` -> lista de clientes
- `GET /api/v1/funds` -> lista de fondos
- `POST /api/v1/subscriptions` -> suscribir `{clientId, fundCode, amount, notificationChannel?}`
- `DELETE /api/v1/subscriptions/{id}?clientId=` -> cancelar y devolver saldo
- `GET /api/v1/subscriptions/transactions?clientId=` -> historial por cliente
- `GET /api/v1/transactions?clientId=` -> historial (endpoint dedicado)

## Swagger / OpenAPI
- UI: `http://localhost:8080/swagger-ui/index.html`
- Docs: `http://localhost:8080/v3/api-docs`

## Arranque rápido leyendo .env (PowerShell)
```
$env:SPRING_PROFILES_ACTIVE="cloud-nosql"
.\scriptsun-with-env.ps1 -Profile cloud-nosql -SkipTests
```
- Usa solo MongoDB. `.env` está git-ignorado.

## Diseño del modelo de datos NoSQL

Para soportar las operaciones requeridas en la prueba técnica, se diseñó un modelo de datos NoSQL usando MongoDB, orientado a documentos. Esta decisión permite representar de forma flexible la información de clientes, fondos, suscripciones, transacciones y notificaciones, manteniendo una estructura simple, escalable y fácil de consultar. La solución cubre las funcionalidades de apertura de fondos, cancelación de suscripciones, consulta de historial y envío de notificaciones según la preferencia del cliente.

### Colecciones principales

**clients** – Información del cliente y saldo disponible.
```json
{
  "_id": "client-001",
  "name": "Juan Pérez",
  "email": "juan@mail.com",
  "phone": "3001234567",
  "balance": 500000,
  "preferredNotification": "EMAIL"
}
```
Campos: `_id`, `name`, `email`, `phone`, `balance`, `preferredNotification` (EMAIL | SMS).

**funds** – Catálogo de fondos y monto mínimo de vinculación.
```json
{
  "_id": "fund-001",
  "code": "FPV_BTG_PACTUAL_RECAUDADORA",
  "name": "FPV_BTG_PACTUAL_RECAUDADORA",
  "minimumAmount": 75000,
  "category": "FPV"
}
```
Campos: `_id`, `code`, `name`, `minimumAmount`, `category` (FPV | FIC).

**subscriptions** – Suscripción activa/cancelada de un cliente a un fondo.
```json
{
  "_id": "sub-001",
  "clientId": "client-001",
  "fundId": "fund-001",
  "amount": 75000,
  "status": "ACTIVE",
  "createdAt": "2026-04-10T10:00:00Z",
  "cancelledAt": null
}
```
Campos: `_id`, `clientId`, `fundId`, `amount`, `status` (ACTIVE | CANCELLED), `createdAt`, `cancelledAt`.

**transactions** – Historial de aperturas/cancelaciones para trazabilidad.
```json
{
  "_id": "txn-001",
  "clientId": "client-001",
  "fundId": "fund-001",
  "subscriptionId": "sub-001",
  "type": "OPENING",
  "amount": 75000,
  "channel": "EMAIL",
  "createdAt": "2026-04-10T10:00:00Z"
}
```
Campos: `_id`, `clientId`, `fundId`, `subscriptionId`, `type` (OPENING | CANCELLATION), `amount`, `channel`, `createdAt`.

**notifications (opcional)** – Evidencia de envío de notificaciones.
```json
{
  "_id": "noti-001",
  "clientId": "client-001",
  "transactionId": "txn-001",
  "channel": "EMAIL",
  "message": "Suscripción realizada con éxito",
  "status": "SENT",
  "sentAt": "2026-04-10T10:00:01Z"
}
```
Campos: `_id`, `clientId`, `transactionId`, `channel`, `message`, `status`, `sentAt`.

### Justificación
- NoSQL con MongoDB facilita la evolución del esquema y consultas rápidas por cliente/fondo/historial.
- Colecciones separadas mantienen responsabilidades claras: clientes (saldo), fondos (reglas), suscripciones (estado), transacciones (trazabilidad) y notificaciones.
- Relaciones lógicas manejadas por IDs (`clientId`, `fundId`, `subscriptionId`, `transactionId`) sin necesidad de joins complejos.

### Consultas soportadas
- Listar fondos disponibles.
- Consultar clientes y saldo.
- Crear/cancelar suscripciones.
- Historial de transacciones por cliente.
- Registrar/consultar notificaciones enviadas.

### Conclusión
El modelo soporta las reglas de negocio de la prueba: monto inicial del cliente, mínimos por fondo, devolución al cancelar, trazabilidad por transacción y respeto al canal de notificación preferido. Diseñado para encajar con el backend en Spring Boot + MongoDB y exponer una API REST moderna.
