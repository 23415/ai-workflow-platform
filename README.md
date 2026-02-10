# 🤖 AI Workflow Platform (Microservices)

An **AI-enabled workflow & ticket management platform** built using **Java, Spring Boot, microservices, and event-driven architecture**, with a strong focus on **backend fundamentals, scalability, and production practices**.

This project is developed with a **real-world backend mindset** and is continuously evolving.

---

## 🎯 Project Vision

Build a production-inspired backend system that demonstrates:

- Clean REST API design  
- Secure authentication & authorization  
- Microservice architecture  
- Event-driven communication  
- Caching & performance optimization  
- AI/LLM integration  
- Resilience, observability, and DevOps practices  

---

## 🧱 Architecture Overview

```
                ┌──────────────┐
                │   Client     │
                │ (Web / API)  │
                └──────┬───────┘
                       │
                ┌──────▼───────┐
                │ Auth Service │
                │ (JWT, Roles) │
                └──────┬───────┘
                       │
        ┌──────────────▼──────────────┐
        │      Workflow Service       │
        │  Tickets, SLA, Comments     │
        └──────┬───────────┬─────────┘
               │           │
        ┌──────▼───┐   ┌───▼────────┐
        │  Redis   │   │   Kafka     │
        │  Cache   │   │  (Events)   │
        └──────────┘   └───┬────────┘
                             │
                      ┌──────▼───────┐
                      │  AI Service  │
                      │ NLP / LLM    │
                      └──────────────┘
```

---

## 🧩 Repository Structure

```
ai-workflow-platform
├── auth-service
├── workflow-service
├── ai-service
├── common-lib
└── docker-compose.yml
```

---

## 🔐 Auth Service (Implemented)

**Responsibilities**
- User registration  
- Login & JWT token generation  
- Role-based access control  

**APIs**
```
POST /api/v1/auth/register
POST /api/v1/auth/login
GET  /api/v1/auth/me
```

**Key Concepts**
- Spring Security filter chain  
- JWT authentication flow  
- Password hashing (BCrypt)  
- DTO vs Entity separation  
- Validation & global exception handling  

---

## 🧠 Workflow Service (Implemented)

**Entities**
- User  
- Ticket  
- Comment  
- SLA  

**Features**
- Create and assign tickets  
- Update ticket status  
- Fetch tickets by role  
- Pagination & indexing  
- SLA tracking  

**Key Concepts**
- JPA mappings  
- N+1 problem handling  
- Service vs Repository layering  
- Clean Architecture principles  

---

## ⚡ Event-Driven Architecture (Kafka)

**Events**
- TicketCreatedEvent  
- TicketUpdatedEvent  
- SLABreachEvent  

**Why Kafka?**
- Decoupled services  
- Asynchronous processing  
- Scalability & fault tolerance  

---

## 🚀 Performance & Scalability

- Redis-based caching  
- Cache-aside pattern  
- TTL-based eviction  
- Role and ticket caching  

---

## 🤖 AI Service (Planned / In Progress)

**Features**
- Ticket resolution suggestions  
- Semantic search  
- AI-assisted workflow  

**Tech**
- OpenAI API / Llama  
- Embeddings  
- FAISS Vector DB  

---

## 🐳 Running Locally

### Prerequisites
- Java 17+
- Docker & Docker Compose
- Maven

### Steps
```bash
git clone https://github.com/your-username/ai-workflow-platform.git
cd ai-workflow-platform
docker-compose up -d
```

Run each service:
```bash
cd auth-service && mvn spring-boot:run
cd workflow-service && mvn spring-boot:run
cd ai-service && mvn spring-boot:run
```

Swagger UI will be available at:
```
http://localhost:{port}/swagger-ui.html
```

---

## 🚧 Upcoming Features

- Refresh token support  
- Rate limiting  
- Circuit breaker (Resilience4j)  
- Centralized logging & metrics  
- Distributed tracing  

---

## 📌 Status

🚧 Actively under development  
