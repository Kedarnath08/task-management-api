# Task Management REST API

A production-style REST API built with Java and Spring Boot. Supports user authentication with JWT, full task CRUD with ownership enforcement, filtering by status and priority, and Gemini AI integration for smart task creation and summaries.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.0.6 |
| Database | MySQL 8 |
| ORM | Spring Data JPA (Hibernate) |
| Security | Spring Security + JWT (JJWT 0.11.5) |
| AI Integration | Google Gemini API |
| Containerisation | Docker + Docker Compose |
| Testing | JUnit 5 + Mockito |
| Code Reduction | Lombok |

---

## Features

- **JWT Authentication** — register and login with BCrypt password hashing and stateless JWT tokens
- **Task CRUD** — create, read, update, delete tasks scoped to the authenticated user
- **Ownership enforcement** — users can only update or delete their own tasks
- **Filtering** — filter tasks by status (`PENDING`, `IN_PROGRESS`, `COMPLETED`) or priority (`LOW`, `MEDIUM`, `HIGH`)
- **AI Smart Create** — describe a task in plain English; Gemini AI extracts the title and priority automatically
- **AI Summary** — get a conversational summary of all your tasks in 2–3 sentences
- **Global exception handling** — consistent JSON error responses with correct HTTP status codes
- **Docker support** — fully containerised with Docker Compose

---

## API Endpoints

### Auth — no token required

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login and receive a JWT token |

**Register request body:**
```json
{
  "name": "Kedar",
  "email": "kedar@example.com",
  "password": "password123"
}
```

**Login request body:**
```json
{
  "email": "kedar@example.com",
  "password": "password123"
}
```

**Auth response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "name": "Kedar",
  "role": "USER"
}
```

---

### Tasks — Bearer token required

Add the token to every request header:
```
Authorization: Bearer <your_token>
```

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/tasks` | Create a new task |
| GET | `/api/tasks` | Get all your tasks |
| GET | `/api/tasks?status=PENDING` | Filter tasks by status |
| GET | `/api/tasks?priority=HIGH` | Filter tasks by priority |
| GET | `/api/tasks/summary` | Get an AI-generated summary of your tasks |
| POST | `/api/tasks/smart-create` | Create a task from a plain English description |
| PUT | `/api/tasks/{id}` | Update a task |
| DELETE | `/api/tasks/{id}` | Delete a task |

**Create task request body:**
```json
{
  "title": "Fix login bug",
  "description": "The login page throws a 500 on wrong password",
  "status": "IN_PROGRESS",
  "priority": "HIGH",
  "dueDate": "2026-07-15T18:00:00"
}
```

**Smart create request body:**
```json
{
  "text": "I need to urgently fix the payment gateway by tomorrow"
}
```
Gemini will extract the title and set priority to HIGH automatically.

**Task response:**
```json
{
  "id": 1,
  "title": "Fix login bug",
  "description": "The login page throws a 500 on wrong password",
  "status": "IN_PROGRESS",
  "priority": "HIGH",
  "dueDate": "2026-07-15T18:00:00",
  "createdAt": "2026-07-01T10:30:00",
  "userEmail": "kedar@example.com"
}
```

**Error response format:**
```json
{
  "status": 404,
  "message": "Task not found or access denied",
  "timestamp": "2026-07-01T10:30:00"
}
```

---

## Running Locally

### Prerequisites
- Java 17
- Maven
- MySQL 8

### Steps

1. **Clone the repository**
   ```bash
   git clone <repo-url>
   cd taskmanager
   ```

2. **Create the database**
   ```sql
   CREATE DATABASE taskmanager;
   ```

3. **Configure application.properties**

   Create `src/main/resources/application.properties` with:
   ```properties
   spring.application.name=taskmanager

   spring.datasource.url=jdbc:mysql://localhost:3306/taskmanager
   spring.datasource.username=root
   spring.datasource.password=your_mysql_password

   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true

   jwt.secret=your-super-secret-key-that-is-at-least-32-characters-long
   jwt.expiration=86400000

   gemini.api.key=your_gemini_api_key
   gemini.api.url=https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent
   ```

4. **Run the app**
   ```bash
   ./mvnw spring-boot:run
   ```

   The API will be available at `http://localhost:8080`

---

## Running with Docker

### Prerequisites
- Docker + Docker Compose

### Steps

1. **Create your `.env` file**
   ```bash
   cp .env.example .env
   ```
   Then open `.env` and fill in your real values.

2. **Build the JAR**
   ```bash
   ./mvnw clean package -DskipTests
   ```

3. **Start all services**
   ```bash
   docker-compose up --build
   ```

   This starts MySQL and the app together. The API will be available at `http://localhost:8080`.

   > MySQL is mapped to port `3307` on your host to avoid conflicts with a locally running MySQL instance.

4. **Stop everything**
   ```bash
   docker-compose down
   ```

---

## Running Tests

```bash
./mvnw test
```

The test suite covers service layer logic, JWT utility, controller endpoints (via MockMvc), and Gemini API integration — all using mocks, no database required.

---

## Project Structure

```
src/main/java/com/kedar/taskmanager/
├── config/          # Spring Security configuration
├── controller/      # REST endpoints
├── dto/             # Request and response objects
├── exception/       # Global exception handler
├── model/           # JPA entities (User, Task)
├── repository/      # Database queries
├── security/        # JWT filter and token utility
└── service/         # Business logic
```
