package com.kedar.taskmanager.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String secret = "your-super-secret-key-that-is-at-least-32-characters-long";
    private final long expiration = 86400000; // 1 day

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", secret);
        ReflectionTestUtils.setField(jwtUtil, "expiration", expiration);
    }

    @Test
    void generateToken_ShouldGenerateValidToken() {
        String email = "test@example.com";
        String token = jwtUtil.generateToken(email);

        assertNotNull(token);
        assertTrue(jwtUtil.isTokenValid(token));
        assertEquals(email, jwtUtil.extractEmail(token));
    }

    @Test
    void extractEmail_ShouldExtractCorrectEmail() {
        String email = "another@example.com";
        String token = jwtUtil.generateToken(email);

        String extracted = jwtUtil.extractEmail(token);
        assertEquals(email, extracted);
    }

    @Test
    void isTokenValid_ShouldReturnTrueForValidToken() {
        String token = jwtUtil.generateToken("user@example.com");
        assertTrue(jwtUtil.isTokenValid(token));
    }

    @Test
    void isTokenValid_ShouldReturnFalseForInvalidOrMalformedToken() {
        assertFalse(jwtUtil.isTokenValid("invalid.token.structure"));
        assertFalse(jwtUtil.isTokenValid(""));
    }

    @Test
    void isTokenValid_ShouldReturnFalseForExpiredToken() {
        // Set short expiration (negative) to make it immediately expired
        ReflectionTestUtils.setField(jwtUtil, "expiration", -1000L);
        String token = jwtUtil.generateToken("expired@example.com");
        assertFalse(jwtUtil.isTokenValid(token));
    }
}
