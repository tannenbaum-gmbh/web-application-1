package com.demo.stockchecker.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for JwtUtil.
 * Tests token generation, validation, and edge cases.
 * 
 * @author Demo Team
 * @version 1.0.0
 */
class JwtUtilTest {

    private JwtUtil jwtUtil;
    private static final String TEST_SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private static final Long TEST_EXPIRATION = 3600000L; // 1 hour

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(TEST_SECRET);
        ReflectionTestUtils.setField(jwtUtil, "expiration", TEST_EXPIRATION);
    }

    @Test
    void generateToken_WithValidUsername_ShouldReturnToken() {
        // Arrange
        String username = "testuser";

        // Act
        String token = jwtUtil.generateToken(username);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3); // JWT has 3 parts separated by dots
    }

    @Test
    void extractUsername_WithValidToken_ShouldReturnUsername() {
        // Arrange
        String username = "testuser";
        String token = jwtUtil.generateToken(username);

        // Act
        String extractedUsername = jwtUtil.extractUsername(token);

        // Assert
        assertEquals(username, extractedUsername);
    }

    @Test
    void extractExpiration_WithValidToken_ShouldReturnFutureDate() {
        // Arrange
        String username = "testuser";
        String token = jwtUtil.generateToken(username);

        // Act
        Date expiration = jwtUtil.extractExpiration(token);

        // Assert
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void validateToken_WithValidToken_ShouldReturnTrue() {
        // Arrange
        String username = "testuser";
        String token = jwtUtil.generateToken(username);

        // Act
        Boolean isValid = jwtUtil.validateToken(token, username);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void validateToken_WithWrongUsername_ShouldReturnFalse() {
        // Arrange
        String username = "testuser";
        String token = jwtUtil.generateToken(username);

        // Act
        Boolean isValid = jwtUtil.validateToken(token, "wronguser");

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validateToken_WithExpiredToken_ShouldReturnFalse() {
        // Arrange
        String username = "testuser";
        JwtUtil shortExpirationJwtUtil = new JwtUtil(TEST_SECRET);
        ReflectionTestUtils.setField(shortExpirationJwtUtil, "expiration", -1000L); // Expired token
        String token = shortExpirationJwtUtil.generateToken(username);

        // Act
        Boolean isValid = jwtUtil.validateToken(token, username);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validateToken_WithMalformedToken_ShouldReturnFalse() {
        // Arrange
        String malformedToken = "not.a.valid.jwt.token";
        String username = "testuser";

        // Act
        Boolean isValid = jwtUtil.validateToken(malformedToken, username);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validateToken_WithNullToken_ShouldReturnFalse() {
        // Arrange
        String username = "testuser";

        // Act
        Boolean isValid = jwtUtil.validateToken(null, username);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validateToken_WithEmptyToken_ShouldReturnFalse() {
        // Arrange
        String username = "testuser";

        // Act
        Boolean isValid = jwtUtil.validateToken("", username);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void extractUsername_WithMalformedToken_ShouldThrowException() {
        // Arrange
        String malformedToken = "not.a.valid.jwt";

        // Act & Assert
        assertThrows(Exception.class, () -> jwtUtil.extractUsername(malformedToken));
    }

    @Test
    void generateToken_WithDifferentUsernames_ShouldGenerateDifferentTokens() {
        // Arrange
        String username1 = "user1";
        String username2 = "user2";

        // Act
        String token1 = jwtUtil.generateToken(username1);
        String token2 = jwtUtil.generateToken(username2);

        // Assert
        assertNotEquals(token1, token2);
    }
}
