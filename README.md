
# 📘 Microservicio de Gestión de Clientes (Client Management Service)

## 🧩 Descripción General
Este proyecto es un microservicio desarrollado en **Spring Boot 3**, diseñado para gestionar clientes dentro del ecosistema **PinApp**. 
Incluye operaciones CRUD, métricas de observabilidad, mensajería asíncrona con RabbitMQ y persistencia en PostgreSQL.

El servicio sigue principios de **Clean Architecture** y buenas prácticas de desarrollo empresarial.

---
## Autor
Oliber Garcia 
oliber.garcia@gmail.com
---

## ⚙️ Tecnologías Utilizadas
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

## 🧱 Arquitectura
El proyecto sigue una arquitectura basada en capas limpias:

```
tech.pinapp.clientmanagement
 ├── application       # Casos de uso y lógica de negocio
 ├── domain            # Entidades del dominio
 ├── infrastructure    # Adaptadores (DB, REST, mensajería, etc.)
 └── config            # Configuración de Spring, seguridad, métricas, etc.
```

---

## 🚀 Ejecución Local

### 1️⃣ Requisitos previos
- Java 21
- Maven 3.9+
- PostgreSQL y RabbitMQ ejecutándose localmente
- Archivo `.env` con variables de entorno

Ejemplo de `.env`:
```bash
DB_URL=jdbc:postgresql://localhost:5432/clientdb
DB_USERNAME=postgres
DB_PASSWORD=admin
RABBIT_HOST=localhost
RABBIT_PORT=5672
RABBIT_USERNAME=guest
RABBIT_PASSWORD=guest
```

### 2️⃣ Compilación y ejecución
```bash
mvn clean package -DskipTests
java -jar target/client-management-service.jar --spring.profiles.active=local
```

---

## 🐳 Ejecución con Docker Compose

```bash
docker compose up --build
```
Esto levantará:
- `client-management-service` (backend)
- `postgres` (base de datos)
- `rabbitmq` (mensajería con panel de control)

Swagger UI estará disponible en: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 🔐 Seguridad
La autenticación está basada en **JWT (JSON Web Tokens)**, gestionada mediante **Spring Security 6**.

---

## 📊 Monitoreo y Métricas
Incluye integración con **Micrometer** y **Prometheus**.  
Endpoints disponibles:
- `/actuator/health`
- `/actuator/metrics`
- `/actuator/prometheus`

---

## 🧪 Tests
Los tests unitarios y de integración usan **JUnit 5** y **Mockito**.

```bash
mvn test
```

---

## 📁 Estructura del Proyecto
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

