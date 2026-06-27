# Concepts — Learned & Pending

## What You Fully Understood

### 1. Annotations
Annotations are instructions you give to Java or a framework, written using the `@` symbol.
They don't change logic — they attach metadata that tells Spring/JPA/Lombok how to treat the code.

Three types used in this project:
- **JPA annotations** — `@Entity`, `@Table`, `@Id`, `@Column`, `@Enumerated` → tell JPA how to map to database
- **Spring annotations** — `@RestController`, `@Service`, `@Repository`, `@Component`, `@Bean` → tell Spring how to manage classes
- **Lombok annotations** — `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@RequiredArgsConstructor` → auto-generate boilerplate code

---

### 2. How Spring Boot Starts Up (Basic Flow)
```
1. Spring Boot starts
2. Scans all packages for annotations
3. Creates objects (Beans) for @Service, @Repository, @Component, @Controller
4. Wires dependencies together (Dependency Injection)
5. JPA reads all @Entity classes → creates tables in MySQL
6. Starts embedded Tomcat server on port 8080
7. App is ready to receive HTTP requests
```

---

### 3. User.java — Every Line
- `@Entity` → map this class to a DB table
- `@Table(name = "users")` → name the table "users"
- `@Data` → Lombok generates getters, setters, toString, equals, hashCode
- `@NoArgsConstructor` → Lombok generates empty constructor (JPA needs this to create objects from DB rows)
- `@Id` → primary key
- `@GeneratedValue(strategy = GenerationType.IDENTITY)` → auto-increment
- `@Column(nullable = false, unique = true)` → DB constraint
- `@Enumerated(EnumType.STRING)` → store enum value as string ("USER"/"ADMIN") not number
- `Role.USER` as default → every new user is USER unless explicitly set to ADMIN

---

### 4. UserRepository.java — Every Line
- It's an **interface** — Spring creates the implementation automatically
- `extends JpaRepository<User, Long>` → inherits save(), findById(), findAll(), delete(), count() for free
- `findByEmail` → Spring reads method name → generates `SELECT * FROM users WHERE email = ?`
- `existsByEmail` → generates `SELECT EXISTS WHERE email = ?`
- `Optional<User>` → safe wrapper for values that might not exist, prevents NullPointerException

---

### 5. DTOs (RegisterRequest, LoginRequest, AuthResponse)
- DTOs = Data Transfer Objects — control what goes IN and OUT of your API
- Entity is for the database. DTO is for the API. Kept separate on purpose.
- `@NotBlank`, `@Email`, `@Size` → input validation annotations
- `@AllArgsConstructor` → Lombok generates constructor with all fields (used in AuthResponse)
- Password is never sent back in response — security practice

---

### 6. Circular Dependency Fix
SecurityConfig and JwtAuthFilter had a circular dependency — each needed 
the other to be created first. Fixed by moving JwtAuthFilter out of the 
constructor and into the filterChain() method parameter instead.


## What Needs to Be Covered in Next Chat

### Priority 1 — Java Basics (cover before continuing project)
- [ ] Classes, objects, fields, methods
- [ ] Access modifiers (public, private, protected)
- [ ] Constructors — what they are, why needed
- [ ] Four pillars of OOP:
  - Encapsulation
  - Inheritance
  - Polymorphism
  - Abstraction

### Priority 2 — Needs One More Pass
- [ ] **Interfaces** — understood partially, needs reinforcement with more examples
- [ ] **Dependency Injection** — understood partially, needs one more clear explanation

### Priority 3 — Files Not Yet Explained
- [ ] JwtUtil.java — start with: what is JWT conceptually, why it exists, then code
- [ ] JwtAuthFilter.java
- [ ] SecurityConfig.java
- [ ] AuthService.java
- [ ] AuthController.java

---

## Key Interview Answers to Remember

**What is the difference between @Entity and @Table?**
> `@Entity` marks the class as a JPA-managed database entity — mandatory. `@Table` is optional and customises the table name. Without `@Table`, the table name defaults to the class name.

**What is the difference between JpaRepository, CrudRepository, and PagingAndSortingRepository?**
> They're a hierarchy. `CrudRepository` gives basic save/find/delete. `PagingAndSortingRepository` adds pagination and sorting. `JpaRepository` extends both and adds JPA-specific methods like `flush()` and `saveAll()`. In practice always use `JpaRepository`.

**What is the difference between a DTO and an Entity?**
> An Entity maps directly to a database table and is managed by JPA. A DTO is a plain Java object used to transfer data between layers — specifically between the API layer and service layer. DTOs control exactly what data is exposed, avoid exposing sensitive fields like passwords, and decouple the API contract from the database structure.

**What is IoC and Dependency Injection?**
> IoC is a design principle where the control of object creation is given to a container (Spring) rather than the developer. Dependency Injection is how Spring implements IoC — it injects required dependencies into a class automatically through constructors. Instead of a class creating its own dependencies with `new`, Spring creates and hands them over.

---

## How to Continue in New Chat

Paste all 4 `.md` files as context and say:

> "Here is the full context of my project and where we left off. MySQL is not yet installed — help me install and set it up first. After that, before continuing with the remaining files, cover the Java basics I still need: Classes/Objects, Four pillars of OOP, then one more pass on Interfaces and Dependency Injection. After that continue explaining the remaining files starting with JwtUtil.java — but first explain what JWT is conceptually before touching any code. Keep the same teaching style — concepts before code, fundamentals built in parallel with the project."
