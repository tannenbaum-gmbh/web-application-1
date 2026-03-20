package com.demo.stockchecker.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for JwtUtil.
 * Tests JWT token generation, validation, and parsing.
 * 
 * @author Demo Team
 * @version 1.0.0
 */
class JwtUtilTest {

    private JwtUtil jwtUtil;

    // Base64-encoded 256-bit key for testing
    private static final String TEST_SECRET = "dGhpc0lzQVZlcnlMb25nU2VjcmV0S2V5Rm9ySldUVG9rZW5HZW5lcmF0aW9uMTIzNDU2Nzg5MA==";
    private static final long TEST_EXPIRATION = 86400000L; // 24 hours

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(TEST_SECRET, TEST_EXPIRATION);
    }

    @Test
    void generateToken_ShouldReturnValidToken() {
        String token = jwtUtil.generateToken("testuser");

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void getUsernameFromToken_ShouldReturnCorrectUsername() {
        String token = jwtUtil.generateToken("testuser");

        String username = jwtUtil.getUsernameFromToken(token);

        assertEquals("testuser", username);
    }

    @Test
    void validateToken_WhenValidToken_ShouldReturnTrue() {
        String token = jwtUtil.generateToken("testuser");

        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void validateToken_WhenInvalidToken_ShouldReturnFalse() {
        assertFalse(jwtUtil.validateToken("invalid.token.here"));
    }

    @Test
    void validateToken_WhenExpiredToken_ShouldReturnFalse() {
        // Create a JwtUtil with 0ms expiration (already expired)
        JwtUtil expiredJwtUtil = new JwtUtil(TEST_SECRET, 0L);
        String token = expiredJwtUtil.generateToken("testuser");

        assertFalse(jwtUtil.validateToken(token));
    }

    @Test
    void validateToken_WhenNullToken_ShouldReturnFalse() {
        assertFalse(jwtUtil.validateToken(null));
    }

    @Test
    void validateToken_WhenEmptyToken_ShouldReturnFalse() {
        assertFalse(jwtUtil.validateToken(""));
    }

    @Test
    void generateToken_DifferentUsernames_ShouldGenerateDifferentTokens() {
        String token1 = jwtUtil.generateToken("user1");
        String token2 = jwtUtil.generateToken("user2");

        assertNotEquals(token1, token2);
    }
}
