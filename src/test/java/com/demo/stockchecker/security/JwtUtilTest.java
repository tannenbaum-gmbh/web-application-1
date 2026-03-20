package com.demo.stockchecker.security;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for JwtUtil.
 */
@SpringBootTest(classes = JwtUtil.class)
@TestPropertySource(properties = {
    "jwt.secret=5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437",
    "jwt.expiration=86400000"
})
class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    private String testUsername;

    @BeforeEach
    void setUp() {
        testUsername = "testuser";
    }

    @Test
    void testGenerateToken() {
        String token = jwtUtil.generateToken(testUsername);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testExtractUsername() {
        String token = jwtUtil.generateToken(testUsername);
        String extractedUsername = jwtUtil.extractUsername(token);

        assertEquals(testUsername, extractedUsername);
    }

    @Test
    void testValidateToken_ValidToken() {
        String token = jwtUtil.generateToken(testUsername);

        assertTrue(jwtUtil.validateToken(token, testUsername));
    }

    @Test
    void testValidateToken_InvalidUsername() {
        String token = jwtUtil.generateToken(testUsername);

        assertFalse(jwtUtil.validateToken(token, "differentuser"));
    }

    @Test
    void testValidateToken_InvalidToken() {
        assertFalse(jwtUtil.validateToken("invalid.token.here"));
    }

    @Test
    void testValidateToken_WithoutUsername() {
        String token = jwtUtil.generateToken(testUsername);

        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void testExtractExpiration() {
        String token = jwtUtil.generateToken(testUsername);

        assertNotNull(jwtUtil.extractExpiration(token));
        assertTrue(jwtUtil.extractExpiration(token).getTime() > System.currentTimeMillis());
    }
}
