# Spring Boot Bug-Fixing Lab

A deliberately flawed, realistic order-management REST API for practicing bug fixing, exception handling, validation, JPA, and automated tests.

## Requirements
- Java 17+
- Maven 3.9+ (or add Maven Wrapper)
- Internet access on first build to download dependencies

This starter uses an in-memory H2 database so you can start without installing MySQL. Data resets when the app stops.

## Run
```bash
mvn spring-boot:run
```

Run tests (they are expected to fail until you fix the bugs):
```bash
mvn test
```

## Endpoints
- `POST /api/orders` — create order
- `GET /api/orders/{id}` — get one order
- `GET /api/orders?email=buyer@example.com` — list by customer email
- `PATCH /api/orders/{id}/cancel` — cancel order
- `PATCH /api/orders/{id}/ship` — mark order shipped

Example request:
```json
{
  "customerEmail": "buyer@example.com",
  "productName": "USB-C Cable",
  "quantity": 2,
  "unitPrice": 9.99
}
```

## Your role: the hired bug-fixing developer

Do not try to fix everything at once. Follow a professional workflow:
1. Run `mvn test` and record the failures.
2. Reproduce one bug with an API request.
3. Identify the root cause before editing code.
4. Add a regression test.
5. Fix the smallest appropriate part of the code.
6. Run tests again.
7. Record the root cause, code change, and verification in `BUG_FIX_REPORT.md`.

## Bug tickets (10)
1. Request accepts missing/invalid email and blank product name.
2. Request accepts zero/negative quantity and negative price.
3. `createdAt` is null after creation.
4. A nonexistent order does not produce HTTP 404.
5. Cancelling a nonexistent order causes an uncontrolled null dereference.
6. A cancelled order can be shipped; invalid status transitions are allowed.
7. Create endpoint returns HTTP 200 instead of HTTP 201.
8. Request validation is not activated with `@Valid`.
9. Global exception handler returns HTTP 200 for failures.
10. Error response exposes raw exception details and has no stable format.

Additional practice:
- Normalize email addresses consistently.
- Add tests for malformed JSON, missing query parameters, and unsupported status transitions.
- Add pagination for list endpoints.
- Add service-layer unit tests using Mockito.
- Do not catch `Exception` indiscriminately; map expected errors to meaningful status codes.

## Suggested response contract
Success: normal order JSON.
Error:
```json
{
  "timestamp": "2026-01-01T12:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Order not found: 123",
  "path": "/api/orders/123"
}
```

## Safety note
This project is intentionally buggy and for local practice only. Do not deploy it publicly before fixing the listed issues.
