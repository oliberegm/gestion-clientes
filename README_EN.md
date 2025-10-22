
# 📘 Client Management Microservice

## 🧩 Overview
This project is a **Spring Boot 3** microservice designed to manage clients within the **PinApp** ecosystem. 
It supports CRUD operations, observability metrics, asynchronous messaging with RabbitMQ, and PostgreSQL persistence.

The service follows **Clean Architecture** principles and enterprise-grade best practices.

---
## Author
Oliber Garcia
oliber.garcia@gmail.com
---

## ⚙️ Technologies Used
- **Java 21**
- **Spring Boot 3.5**
- **Spring Data JPA (PostgreSQL)**
- **Spring Security + JWT**
- **Spring AMQP (RabbitMQ)**
- **Micrometer + Prometheus + Actuator**
- **OpenAPI / Swagger UI**
- **Docker + Docker Compose**
- **Logback + Logstash Encoder (JSON Logs)**

---

## 🧱 Architecture
The project follows a clean layered structure:

```
tech.pinapp.clientmanagement
 ├── application       # Use cases and business logic
 ├── domain            # Domain entities
 ├── infrastructure    # Adapters (DB, REST, messaging, etc.)
 └── config            # Spring configuration, security, metrics, etc.
```

---

## 🚀 Running Locally

### 1️⃣ Prerequisites
- Java 21
- Maven 3.9+
- PostgreSQL and RabbitMQ running locally
- `.env` file with environment variables

Example `.env` file:
```bash
DB_URL=jdbc:postgresql://localhost:5432/clientdb
DB_USERNAME=postgres
DB_PASSWORD=admin
RABBIT_HOST=localhost
RABBIT_PORT=5672
RABBIT_USERNAME=guest
RABBIT_PASSWORD=guest
```

### 2️⃣ Build and Run
```bash
mvn clean package -DskipTests
java -jar target/client-management-service.jar --spring.profiles.active=local
```

---

## 🐳 Running with Docker Compose

```bash
docker compose up --build
```
This will start:
- `client-management-service` (backend)
- `postgres` (database)
- `rabbitmq` (messaging with management console)

Swagger UI available at: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 🔐 Security
Authentication is based on **JWT (JSON Web Tokens)** handled through **Spring Security 6**.

---

## 📊 Monitoring and Metrics
Integrated with **Micrometer** and **Prometheus**.  
Available endpoints:
- `/actuator/health`
- `/actuator/metrics`
- `/actuator/prometheus`

---

## 🧪 Testing
Unit and integration tests are implemented using **JUnit 5** and **Mockito**.

```bash
mvn test
```

---

## 📁 Project Structure
```
.
├── src/
│   ├── main/
│   │   ├── java/tech/pinapp/clientmanagement/
│   │   └── resources/
│   │       ├── application.yml
│   │       └── logback-spring.xml
├── Dockerfile
├── docker-compose.yml
├── .env
└── pom.xml
```

---
