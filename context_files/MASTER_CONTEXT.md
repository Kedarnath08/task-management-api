# MASTER CONTEXT — Kedarnath Kini | Java Backend Job Search + Project Build

> Paste this entire file at the start of any new conversation with ChatGPT, Claude, or Claude Code.
> It contains: profile, resume status, job search strategy, full project code, concepts learned, and exact roadmap.

---

## 1. WHO I AM

- **Name:** Kedarnath Kini
- **Role:** Software Trainee at Estuate Softwares, Bengaluru (March 2025 – Present)
- **Experience:** ~15 months professional experience
- **Education:** B.E. Information Science & Engineering, Atria Institute of Technology — 8.6 CGPA
- **Goal:** Land a Java Backend Developer job (0-2 years experience level) in Bengaluru
- **Email:** kedarkini2019@gmail.com
- **LinkedIn:** https://www.linkedin.com/in/kedarnath-kini/
- **GitHub:** New account to be created (old one being abandoned)

---

## 2. MY TECH STACK

| Level | Skills |
|---|---|
| Strong | Java, Spring Boot, REST APIs, JWT, MySQL, PostgreSQL, Spring Data JPA |
| Moderate | React Native (used in office — NOT my target role) |
| Basic only | Python (no frameworks — no Flask, Django, FastAPI) |
| Exposure | RAG, Vector Embeddings, Pinecone, Gemini API |
| Tools | Git, Postman, Swagger, Docker (basic awareness only) |
| Does NOT know | Docker in depth, DevOps, deployment pipelines |

---

## 3. JOB SEARCH CONTEXT

**Problem:** Applied to many jobs — zero interviews. Resume was unfocused.

**Resume was rebuilt to position me as:** Java Backend Developer

**Resume has these 3 projects:**
1. Task Management REST API (Spring Boot, MySQL, JWT, Docker, Gemini API) — 2026
2. RAG AI Assistant (Spring Boot, Python, Pinecone, Gemini API) — Aug–Sep 2025
3. Community Engagement API — Zencoo (Spring Boot, MySQL, JWT, Spring Data JPA) — May–Jun 2025

> Task Management API is on my resume but NOT YET BUILT. Must complete before applying.

**Target roles:** Junior Java Backend Developer, Java Developer (0-2 yrs)

**Target platforms:** Instahyre, Wellfound, LinkedIn Easy Apply, Cutshort.io, Naukri

---

## 4. THE PROJECT I AM BUILDING

### Task Management REST API

**Purpose:** Primary portfolio project to prove Java backend skills to interviewers.

**Stack:** Java 17, Spring Boot 3.2.x, MySQL, Spring Data JPA, Spring Security, JWT (JJWT 0.11.5), Lombok, Jakarta Validation, Gemini API, Docker, JUnit + Mockito

**Project details:**
- Generated from: https://start.spring.io
- Group: com.kedar | Artifact: taskmanager | Packaging: Jar | Java: 17
- IDE: VS Code with Extension Pack for Java, Spring Boot Extension Pack

**JJWT added manually to pom.xml (NOT on start.spring.io):**
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

---

## 5. COMPLETE FOLDER STRUCTURE

```
taskmanager/
├── src/main/java/com/kedar/taskmanager/
│   ├── config/
│   │   └── SecurityConfig.java          DONE
│   ├── controller/
│   │   ├── AuthController.java          DONE
│   │   └── TaskController.java          NOT YET BUILT
│   ├── service/
│   │   ├── AuthService.java             DONE
│   │   ├── TaskService.java             NOT YET BUILT
│   │   └── GeminiService.java           NOT YET BUILT
│   ├── repository/
│   │   ├── UserRepository.java          DONE
│   │   └── TaskRepository.java          NOT YET BUILT
│   ├── model/
│   │   ├── User.java                    DONE
│   │   └── Task.java                    NOT YET BUILT
│   ├── dto/
│   │   ├── RegisterRequest.java         DONE
│   │   ├── LoginRequest.java            DONE
│   │   ├── AuthResponse.java            DONE
│   │   ├── TaskRequest.java             NOT YET BUILT
│   │   └── TaskResponse.java            NOT YET BUILT
│   ├── security/
│   │   ├── JwtUtil.java                 DONE
│   │   └── JwtAuthFilter.java           DONE
│   └── exception/
│       ├── GlobalExceptionHandler.java  NOT YET BUILT
│       └── ResourceNotFoundException.java NOT YET BUILT
├── src/main/resources/
│   └── application.properties           DONE
├── src/test/java/
│   └── TaskServiceTest.java             NOT YET WRITTEN
├── Dockerfile                           NOT YET WRITTEN
├── docker-compose.yml                   NOT YET WRITTEN
└── README.md                            NOT YET WRITTEN
```

---

## 6. ROADMAP & PROGRESS (~20% complete)

### Phase 1 — Foundation — COMPLETE
- Project setup, pom.xml, JJWT, folders, application.properties, User.java

### Phase 2 — Authentication — Code written, MySQL not installed yet
- All auth files written (UserRepository, DTOs, JwtUtil, JwtAuthFilter, SecurityConfig, AuthService, AuthController)
- BLOCKED: MySQL not installed. Install MySQL, create database `taskmanager`, then run app and test /api/auth/register

### Phase 3 — Task CRUD — Not started
- Task.java, TaskRequest/Response DTOs, TaskRepository, TaskService (CRUD + pagination + filtering), TaskController

### Phase 4 — Polish — Not started
- GlobalExceptionHandler, ResourceNotFoundException, GeminiService (Gemini API integration), Unit tests

### Phase 5 — Deployment & GitHub — Not started
- Dockerfile, docker-compose.yml, README.md, push to new GitHub account

---

## 7. ALL CODE WRITTEN SO FAR

### application.properties
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/taskmanager
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD_HERE
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
jwt.secret=your-super-secret-key-that-is-at-least-32-characters-long
jwt.expiration=86400000
```

### model/User.java
```java
package com.kedar.taskmanager.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    public enum Role {
        USER, ADMIN
    }
}
```

### repository/UserRepository.java
```java
package com.kedar.taskmanager.repository;

import com.kedar.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
```

### dto/RegisterRequest.java
```java
package com.kedar.taskmanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}
```

### dto/LoginRequest.java
```java
package com.kedar.taskmanager.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
```

### dto/AuthResponse.java
```java
package com.kedar.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String name;
    private String role;
}
```

### security/JwtUtil.java
```java
package com.kedar.taskmanager.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
```

### security/JwtAuthFilter.java
```java
package com.kedar.taskmanager.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authHeader.substring(7);
        if (jwtUtil.isTokenValid(token)) {
            String email = jwtUtil.extractEmail(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}
```

### config/SecurityConfig.java
```java
package com.kedar.taskmanager.config;

import com.kedar.taskmanager.repository.UserRepository;
import com.kedar.taskmanager.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> userRepository.findByEmail(email)
                .map(user -> org.springframework.security.core.userdetails.User
                        .withUsername(user.getEmail())
                        .password(user.getPassword())
                        .roles(user.getRole().name())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
```

### service/AuthService.java
```java
package com.kedar.taskmanager.service;

import com.kedar.taskmanager.dto.*;
import com.kedar.taskmanager.model.User;
import com.kedar.taskmanager.repository.UserRepository;
import com.kedar.taskmanager.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user.getName(), user.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user.getName(), user.getRole().name());
    }
}
```

### controller/AuthController.java
```java
package com.kedar.taskmanager.controller;

import com.kedar.taskmanager.dto.*;
import com.kedar.taskmanager.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
```

---

## 8. CONCEPTS LEARNED

### Fully understood
- Annotations (JPA, Spring, Lombok types)
- How Spring Boot starts up
- User.java — every line
- UserRepository.java — every line
- DTOs — RegisterRequest, LoginRequest, AuthResponse

### Needs one more pass
- Interfaces
- Dependency Injection

### Not yet covered — do these before continuing with code
- Classes, objects, fields, methods
- Four pillars of OOP (Encapsulation, Inheritance, Polymorphism, Abstraction)

### Files written but NOT yet explained
- JwtUtil.java, JwtAuthFilter.java, SecurityConfig.java, AuthService.java, AuthController.java

---

## 9. KEY INTERVIEW ANSWERS

**@Entity vs @Table?**
`@Entity` marks the class as JPA-managed — mandatory. `@Table` is optional, customises the table name. Without it, table name = class name.

**JpaRepository vs CrudRepository vs PagingAndSortingRepository?**
Hierarchy: CrudRepository (basic CRUD) → PagingAndSortingRepository (adds pagination) → JpaRepository (adds JPA-specific methods). Always use JpaRepository.

**DTO vs Entity?**
Entity maps to a DB table, managed by JPA. DTO is a plain Java object for transferring data between API and service layers. Keeps API contract decoupled from DB structure, prevents leaking sensitive fields.

**IoC and Dependency Injection?**
IoC = Spring controls object creation instead of the developer. DI = how Spring implements it, injecting required dependencies via constructors. You declare what you need, Spring provides it.

---

## 10. HOW TO USE THIS FILE

### For ChatGPT or Claude (web)
Paste the entire content of this file and say:
"This is my full project context. My immediate next step is installing MySQL. After setup, continue explaining remaining files starting with JwtUtil.java — but first cover Java basics I still need: classes/objects, four pillars of OOP, then one more pass on interfaces and DI. Always explain concepts before code."

### For Claude Code in VS Code
1. Save this file as CONTEXT.md in the root of your project folder
2. Open the project in VS Code
3. Start Claude Code and say: "Read CONTEXT.md first — it has my full background, all code, and what needs to be built next."

### My teaching style preference
- Concept before code, always
- Explain every annotation and line when introducing a new file
- I am a beginner — do not assume deep Java knowledge
- Connect new concepts back to what was already explained
