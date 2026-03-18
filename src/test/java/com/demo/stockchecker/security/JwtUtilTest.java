package com.demo.stockchecker.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for JwtUtil.
 *
 * @author Demo Team
 * @version 1.0.0
 */
class JwtUtilTest {

    private JwtUtil jwtUtil;
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_SECRET = "testSecretKeyForStockCheckerApplicationThatIsAtLeast256BitsLongForHS256Algorithm";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", TEST_SECRET);
        ReflectionTestUtils.setField(jwtUtil, "expiration", 86400000L); // 24 hours
    }

    @Test
    void generateToken_ShouldCreateValidToken() {
        // Act
        String token = jwtUtil.generateToken(TEST_USERNAME);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3); // JWT has 3 parts: header, payload, signature
    }

    @Test
    void extractUsername_ShouldReturnCorrectUsername() {
        // Arrange
        String token = jwtUtil.generateToken(TEST_USERNAME);

        // Act
        String username = jwtUtil.extractUsername(token);

        // Assert
        assertEquals(TEST_USERNAME, username);
    }

    @Test
    void validateToken_WithValidToken_ShouldReturnTrue() {
        // Arrange
        String token = jwtUtil.generateToken(TEST_USERNAME);

        // Act
        Boolean isValid = jwtUtil.validateToken(token, TEST_USERNAME);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void validateToken_WithInvalidUsername_ShouldReturnFalse() {
        // Arrange
        String token = jwtUtil.generateToken(TEST_USERNAME);

        // Act
        Boolean isValid = jwtUtil.validateToken(token, "wronguser");

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validateToken_WithExpiredToken_ShouldReturnFalse() throws InterruptedException {
        // Arrange
        ReflectionTestUtils.setField(jwtUtil, "expiration", 100L); // Very short expiration
        String token = jwtUtil.generateToken(TEST_USERNAME);

        // Wait for token to expire
        Thread.sleep(200);

        // Reset expiration to avoid issues with extractUsername
        ReflectionTestUtils.setField(jwtUtil, "expiration", 86400000L);

        // Act - Use the simpler validateToken method that catches exceptions
        Boolean isValid = jwtUtil.validateToken(token);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void validateToken_WithoutUsername_ShouldReturnTrue() {
        // Arrange
        String token = jwtUtil.generateToken(TEST_USERNAME);

        // Act
        Boolean isValid = jwtUtil.validateToken(token);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void validateToken_WithInvalidToken_ShouldReturnFalse() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act
        Boolean isValid = jwtUtil.validateToken(invalidToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void extractExpiration_ShouldReturnFutureDate() {
        // Arrange
        String token = jwtUtil.generateToken(TEST_USERNAME);

        // Act
        var expirationDate = jwtUtil.extractExpiration(token);

        // Assert
        assertNotNull(expirationDate);
        assertTrue(expirationDate.after(new java.util.Date()));
    }
}
