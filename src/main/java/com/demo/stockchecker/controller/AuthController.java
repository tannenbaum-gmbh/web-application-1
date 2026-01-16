package com.demo.stockchecker.controller;

import com.demo.stockchecker.security.JwtUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST Controller for authentication operations.
 * Provides endpoint for JWT token generation.
 * 
 * @author Demo Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final JwtUtil jwtUtil;

    /**
     * Constructor with dependency injection.
     * 
     * @param jwtUtil JWT utility for token operations
     */
    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Authenticates user and generates JWT token.
     * 
     * WARNING: This is a simplified demo implementation that does NOT perform actual authentication.
     * It generates a JWT token for ANY username without validating credentials.
     * 
     * SECURITY RISK: This implementation is INSECURE and MUST NOT be used in production.
     * 
     * For production use, you MUST:
     * - Validate username and password against a secure user database
     * - Use proper password hashing (e.g., BCrypt)
     * - Implement rate limiting to prevent brute force attacks
     * - Add account lockout mechanisms
     * - Implement proper user authentication with Spring Security's AuthenticationManager
     * 
     * @param loginRequest login request containing username
     * @return ResponseEntity with JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("POST /api/auth/login - Authenticating user: {}", loginRequest.getUsername());
        
        // WARNING: No actual authentication is performed here - this is for demo purposes only
        // In production, validate credentials before generating token
        String token = jwtUtil.generateToken(loginRequest.getUsername());
        
        return ResponseEntity.ok(Map.of(
            "token", token,
            "type", "Bearer",
            "username", loginRequest.getUsername()
        ));
    }

    /**
     * Login request DTO.
     */
    public static class LoginRequest {
        @NotBlank(message = "Username is required")
        private String username;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
