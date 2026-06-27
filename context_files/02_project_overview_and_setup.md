# Task Management REST API — Project Overview & Setup

## Why This Project
- Primary portfolio project to prove Java backend skills to interviewers
- Production-style: not a tutorial app, built with real patterns
- Includes AI integration (Gemini API) — differentiator for freshers
- Will be pushed to a brand new GitHub account

---

## Tech Stack
| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2.x |
| Database | MySQL |
| ORM | Spring Data JPA (Hibernate) |
| Security | Spring Security + JWT (JJWT 0.11.5) |
| Code Reduction | Lombok |
| Validation | Jakarta Validation |
| AI Integration | Gemini API |
| Containerisation | Docker + docker-compose |
| Testing | JUnit + Mockito |
| IDE | VS Code |

---

## VS Code Extensions to Install
- Extension Pack for Java (Microsoft)
- Spring Boot Extension Pack (VMware)
- REST Client (Huachao Mao)

---

## Project Generated From
**https://start.spring.io** with these settings:

| Field | Value |
|---|---|
| Project | Maven |
| Language | Java |
| Spring Boot | 3.2.x |
| Group | com.kedar |
| Artifact | taskmanager |
| Packaging | Jar |
| Java | 17 |

**Dependencies added on start.spring.io:**
- Spring Web
- Spring Data JPA
- Spring Security
- MySQL Driver
- Lombok
- Validation

---

## JJWT — Added Manually to pom.xml
JJWT is NOT on start.spring.io. Add these 3 blocks inside `<dependencies>` in `pom.xml`:

```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
```

After saving pom.xml, click **Yes** when VS Code asks to synchronise.

---

## Folder Structure
```
taskmanager/
├── src/main/java/com/kedar/taskmanager/
│   ├── config/
│   │   ├── SecurityConfig.java
│   │   └── ApplicationConfig.java
│   ├── controller/
│   │   ├── AuthController.java
│   │   └── TaskController.java
│   ├── service/
│   │   ├── AuthService.java
│   │   ├── TaskService.java
│   │   └── GeminiService.java
│   ├── repository/
│   │   ├── UserRepository.java
│   │   └── TaskRepository.java
│   ├── model/
│   │   ├── User.java
│   │   └── Task.java
│   ├── dto/
│   │   ├── RegisterRequest.java
│   │   ├── LoginRequest.java
│   │   ├── AuthResponse.java
│   │   ├── TaskRequest.java
│   │   └── TaskResponse.java
│   ├── security/
│   │   ├── JwtUtil.java
│   │   └── JwtAuthFilter.java
│   └── exception/
│       ├── GlobalExceptionHandler.java
│       └── ResourceNotFoundException.java
├── src/main/resources/
│   └── application.properties
├── src/test/java/
│   └── TaskServiceTest.java
├── Dockerfile
├── docker-compose.yml
└── README.md
```

---

## Each Folder's Job
| Folder | Job |
|---|---|
| `model` | Database tables as Java classes |
| `dto` | What you send/receive in API requests |
| `repository` | Database queries |
| `service` | Business logic — the brain |
| `controller` | API endpoints — the door |
| `security` | JWT filter + token logic |
| `config` | Spring Security rules |
| `exception` | Global error handling |

---

## Build Order (Follow This Sequence)

**Week 1**
1. Create project on start.spring.io, add JJWT to pom.xml
2. Connect to MySQL, create database, test connection
3. Build User entity + UserRepository
4. Build Task entity + TaskRepository
5. Build registration API (no security yet)
6. Add JWT — JwtUtil + JwtAuthFilter + login API
7. Protect task endpoints with JWT

**Week 2**
8. Build full Task CRUD (create, read, update, delete)
9. Add pagination + filtering (by status, priority)
10. Add GlobalExceptionHandler
11. Add input validation
12. Add Gemini API integration

**Week 3**
13. Write Dockerfile + docker-compose.yml
14. Write 3-5 unit tests with JUnit + Mockito
15. Write README with Postman screenshots
16. Push to new GitHub account

---

## Current Status
| Task | Status |
|---|---|
| Project generated from start.spring.io | ✅ Done |
| JJWT added to pom.xml | ✅ Done |
| All folders created | ✅ Done |
| All files copy-pasted | ✅ Done |
| MySQL installed | ✅ Done |
| MySQL Workbench set up | ✅ Done |
| Database `taskmanager` created | ✅ Done |
| App run and tested | ✅ Done |
| Task entity + TaskRepository | ❌ Not yet built |
| Task CRUD APIs | ❌ Not yet built |
| GlobalExceptionHandler | ❌ Not yet built |
| Gemini API integration | ❌ Not yet built |
| Docker setup | ❌ Not yet done |
| Unit tests | ❌ Not yet written |
| README | ❌ Not yet written |
