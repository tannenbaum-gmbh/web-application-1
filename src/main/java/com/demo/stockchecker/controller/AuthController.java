package com.demo.stockchecker.controller;

import com.demo.stockchecker.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Authentication controller for JWT token generation.
 * Provides login endpoint that returns JWT tokens.
 * 
 * @author Demo Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Authenticates a user and returns a JWT token.
     * For demo purposes, accepts any username/password combination.
     * 
     * @param loginRequest map containing "username" and "password"
     * @return JWT token response
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        logger.info("Login attempt for user: {}", username);

        if (username == null || username.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Username is required"
            ));
        }

        if (password == null || password.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Password is required"
            ));
        }

        String token = jwtUtil.generateToken(username);

        logger.info("Token generated for user: {}", username);

        return ResponseEntity.ok(Map.of(
            "token", token,
            "type", "Bearer",
            "username", username
        ));
    }
}
