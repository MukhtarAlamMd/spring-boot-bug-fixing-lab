# Spring Boot Bug Fixing Lab

A Java Spring Boot REST API project demonstrating order management, request validation, exception handling, database persistence, and automated testing.

## Overview

This project provides a backend API for creating and managing customer orders. It is designed as a practical learning and portfolio project focused on building reliable REST APIs and testing Spring Boot applications.

## Features

- REST API for order management
- Request validation for order input
- Service-layer business logic
- Persistence using Spring Data JPA and Hibernate
- H2 in-memory database for local development
- Centralized exception handling
- Automated service, repository, and API integration tests

## Technology Stack

- **Language:** Java
- **Framework:** Spring Boot
- **Database:** H2 in-memory database
- **Persistence:** Spring Data JPA and Hibernate
- **Build tool:** Maven
- **Testing:** JUnit and Spring Boot testing tools

## Project Structure

```text
src/
├── main/
│   ├── java/com/practice/buglab/
│   │   ├── controller/
│   │   │   └── OrderController.java
│   │   ├── dto/
│   │   │   └── CreateOrderRequest.java
│   │   ├── exception/
│   │   │   └── GlobalExceptionHandler.java
│   │   ├── model/
│   │   │   ├── CustomerOrder.java
│   │   │   └── OrderStatus.java
│   │   ├── repository/
│   │   │   └── OrderRepository.java
│   │   ├── service/
│   │   │   └── OrderService.java
│   │   └── BugFixingLabApplication.java
│   └── resources/
│       └── application.yml
└── test/java/com/practice/buglab/
    ├── OrderApiIntegrationTest.java
    ├── OrderRepositoryTest.java
    └── OrderServiceTest.java
```
## API Documentation

Base URL: `http://localhost:8080`

### Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/orders` | Create a new order |
| `GET` | `/api/orders` | Retrieve all orders |
| `GET` | `/api/orders/{id}` | Retrieve an order by ID |
| `GET` | `/api/orders?email={email}` | Filter orders by customer email |
| `PATCH` | `/api/orders/{id}/cancel` | Cancel an order |
| `PATCH` | `/api/orders/{id}/ship` | Mark an order as shipped |

### 1. Create an order

**Request**

```http
POST /api/orders
Content-Type: application/json
```

```json
{
  "customerEmail": "buyer@example.com",
  "productName": "USB-C Cable",
  "quantity": 2,
  "unitPrice": 9.99
}
```

**Expected result:** `201 Created`, with the created order returned as JSON. The response includes fields such as the order ID and status, depending on the application configuration and model.

### 2. Get all orders

```http
GET /api/orders
```

Returns the list of orders.

### 3. Get an order by ID

```http
GET /api/orders/1
```

Returns the order with ID `1`. If the order doesn't exist, the API's exception handling returns an error response.

### 4. Filter orders by customer email

```http
GET /api/orders?email=buyer%40example.com
```

Returns orders matching the supplied customer email. URL-encode special characters where necessary.

### 5. Cancel an order

```http
PATCH /api/orders/1/cancel
```

Requests cancellation of order `1`, subject to the service's business rules.

### 6. Mark an order as shipped

```http
PATCH /api/orders/1/ship
```

Requests that order `1` be marked as shipped, subject to the service's business rules.

### Validation and Error Handling

The create-order endpoint uses Jakarta Bean Validation through `@Valid`. Invalid requests may return a validation error response. The application also includes a centralized exception handler.

Exact error formats and allowed status transitions are determined by the service and exception-handler implementations.

## Prerequisites

Install the following before running the application:

- Java version compatible with the project configuration in `pom.xml`
- Apache Maven, or the Maven Wrapper if included in the repository
- Git (optional, for cloning the repository)

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/MukhtarAlamMd/spring-boot-bug-fixing-lab.git
cd spring-boot-bug-fixing-lab
```

### 2. Run the tests

If Maven is installed:

```bash
mvn test
```

If the repository includes the Maven Wrapper, use the appropriate command for your operating system.

Windows PowerShell:

```powershell
.\mvnw.cmd test
```

### 3. Start the application

With Maven installed:

```bash
mvn spring-boot:run
```

Or, if the Maven Wrapper is available on Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

The application is configured to run locally on port `8080`, unless overridden by configuration.

## API Documentation

The API is implemented in `OrderController.java`. Review that class for the exact routes, request formats, and response structures.

The following are common REST operations to document once verified against the controller:

| Operation | Purpose |
|---|---|
| Create order | Submit a new order |
| Get order | Retrieve an order by ID |
| List orders | Retrieve orders |
| Update order | Only if implemented |
| Delete order | Only if implemented |

### Example request

The exact fields depend on `CreateOrderRequest.java`. A typical order-creation request may look like this:

```http
POST /api/orders
Content-Type: application/json
```

```json
{
  "customerEmail": "buyer@example.com",
  "productName": "USB-C Cable",
  "quantity": 2,
  "unitPrice": 9.99
}
```

Example values are illustrative. Confirm the required fields and validation rules in the source code before using this request.

## Testing

The project includes three test classes:

- `OrderApiIntegrationTest` — API integration testing
- `OrderRepositoryTest` — repository and persistence testing
- `OrderServiceTest` — service-layer testing

Run the full suite with `mvn test`. Confirm that the build succeeds before reporting test results.

## Database

The default configuration uses an H2 in-memory database for local development. Data is not intended to persist across application restarts.

## Current Scope and Limitations

This is a portfolio and learning project. The default H2 configuration is intended for development and testing, not production deployment. Production use would require environment-specific configuration, appropriate security controls, and further operational testing.

## Author

**GitHub:** [MukhtarAlamMd](https://github.com/MukhtarAlamMd)

## License

No license has been specified yet. Contact the repository owner before reusing this project or its code.
