# 🚗 Tuffy Ride Service

**Tuffy** is a modern, event-driven **ride-hailing platform** built using **Spring Boot microservices**.  
It follows **clean architecture** and **SOLID principles** to ensure modularity, scalability, and maintainability.

---

## 🧩 Overview

Tuffy provides a complete backend foundation for a ride-sharing application — similar to Uber or Bolt — where users can request rides, drivers can accept and complete them, and real-time location tracking ensures accurate ride matching.

The system is composed of independently deployable microservices that communicate through **Kafka** in an **event-driven architecture**.

---

## 🏗️ Architecture

```
                   ┌──────────────────────────┐
                   │   API Gateway (planned)  │
                   └──────────────────────────┘
                             │
 ┌─────────────────────────────────────────────────────────────────────┐
 │                          Kafka (Confluent Cloud)                    │
 └─────────────────────────────────────────────────────────────────────┘
       ↑                ↑                ↑                ↑
 ┌──────────────┐ ┌──────────────┐ ┌──────────────┐ ┌──────────────┐
 │ User Service │ │ Ride Service │ │ Tracking Svc │ │ Matching Svc │
 └──────────────┘ └──────────────┘ └──────────────┘ └──────────────┘
       ↑                ↑                ↑                ↑
       └──────────────────────────────────────────────────┘
                        │
           ┌───────────────────────────┐
           │   Redis (Upstash Cloud)   │
           └───────────────────────────┘
                        │
           ┌───────────────────────────┐
           │ Oracle Autonomous Database │
           └───────────────────────────┘
```

---

## ⚙️ Current Microservices

### 🧍‍♀️ 1. User Service
Handles user registration, authentication, and profile management.

- **Responsibilities:**
  - Register riders and drivers
  - Handle login and JWT-based authentication
  - Manage user profiles
  - Send email confirmations via **SendGrid**
- **Tech:** Spring Boot, Spring Security, JPA, Oracle DB

---

### 🚕 2. Ride Service
Manages ride lifecycle — from request to completion.

- **Responsibilities:**
  - Request, start, and end rides
  - Persist ride data
  - Publish and consume ride-related Kafka events
- **Tech:** Spring Boot, Spring Data JPA, Kafka, Oracle DB

---

### 📍 3. Tracking Service
Handles driver location updates and lookups.

- **Responsibilities:**
  - Receive continuous location updates from drivers
  - Cache geolocations in **Redis (Upstash)**
  - Provide driver proximity data to the Matching Service
- **Tech:** Spring Boot, Redis, Kafka

---

### 🔄 4. Matching Service
Matches riders with nearby available drivers.

- **Responsibilities:**
  - Consume ride requests and driver location events from Kafka
  - Perform proximity-based driver matching
  - Notify relevant services of successful matches
- **Tech:** Spring Boot, Kafka, Redis

---

## 🕒 Planned Microservices

### 💳 Payment Service *(upcoming)*
Will handle fare calculation, transactions, and payment confirmations.

### 🔔 Notification Service *(upcoming)*
Will manage real-time notifications (email, push, or SMS).

---

## 🔌 Integrations

| Integration | Purpose |
|--------------|----------|
| **Kafka (Confluent Cloud)** | Event-driven communication between services |
| **Swagger / OpenAPI** | REST API documentation |
| **SendGrid** | Email notifications for user verification |
| **Redis (Upstash)** | Caching driver locations and geospatial queries |
| **Oracle Cloud DB** | Primary relational database for persistence |

---

## ☁️ Deployment

| Platform | Purpose |
|-----------|----------|
| **Render** | Hosting and deploying microservices (Dockerized) |
| **Confluent Cloud** | Kafka cluster for asynchronous event streaming |
| **Upstash** | Managed Redis for caching and location tracking |
| **Oracle Cloud Free Tier** | Autonomous SQL Database for persistence |

Each service includes a **multi-stage Dockerfile** optimized for Render’s free-tier deployment.

---

## 🧰 Tech Stack

| Layer | Technologies |
|-------|---------------|
| Language | Java 17 |
| Framework | Spring Boot 3, Spring Security, Spring Data JPA |
| Communication | REST, Kafka (event-driven) |
| Database | Oracle Autonomous DB |
| Caching | Redis (Upstash) |
| CI/CD | Render (Docker-based) |
| Documentation | Swagger / OpenAPI |
| Mail | SendGrid API |

---

## 🚀 Running Locally

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/tuffy.git
   cd tuffy
   ```

2. **Navigate to a service**
   ```bash
   cd user-service
   ```

3. **Set environment variables**
   ```bash
   export DB_URL=jdbc:oracle:thin:@your_db_tp?TNS_ADMIN=/path/to/wallet
   export DB_USER=admin
   export DB_PASSWORD=your_password
   export KAFKA_BOOTSTRAP_SERVERS=your-confluent-url:9092
   ```

4. **Run the service**
   ```bash
   mvn clean package
   java -jar target/*.jar
   ```

5. **Access health check**
   ```bash
   http://localhost:8080/actuator/health
   ```

---

## 🧪 Testing

Each microservice includes:
- Unit tests with **JUnit 5** and **Mockito**
- Integration tests (WIP)
- Testcontainers planned for Kafka and DB integration

---

## 🗺️ Roadmap

- [ ] Implement API Gateway (Spring Cloud Gateway)
- [ ] Add Payment and Notification services
- [ ] Add Prometheus/Grafana monitoring
- [ ] Docker Compose setup for local orchestration
- [ ] GitHub Actions for CI/CD

---

## 👤 Author

**Haidy Elnahass**  
Senior Java Developer specializing in **Spring Boot**, **Kafka**, **Docker**, and **microservice architecture**.

---
