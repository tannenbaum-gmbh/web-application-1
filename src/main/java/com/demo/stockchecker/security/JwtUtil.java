package com.demo.stockchecker.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utility class for JWT token operations.
 * Handles token generation, validation, and claims extraction.
 * 
 * @author Demo Team
 * @version 1.0.0
 */
@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    private final SecretKey signingKey;

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * Constructor that initializes the signing key.
     * 
     * @param secret JWT secret from configuration
     */
    public JwtUtil(@Value("${jwt.secret}") String secret) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generates JWT token for a username.
     * 
     * @param username the username
     * @return JWT token string
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    /**
     * Creates a JWT token with claims and subject.
     * 
     * @param claims additional claims to include
     * @param subject the token subject (username)
     * @return JWT token string
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(signingKey)
                .compact();
    }

    /**
     * Extracts username from JWT token.
     * 
     * @param token JWT token
     * @return username from token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts expiration date from JWT token.
     * 
     * @param token JWT token
     * @return expiration date
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from JWT token.
     * 
     * @param token JWT token
     * @param claimsResolver function to extract claim
     * @param <T> type of claim value
     * @return claim value
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from JWT token.
     * 
     * @param token JWT token
     * @return claims object
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Checks if token is expired.
     * 
     * @param token JWT token
     * @return true if expired, false otherwise
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Validates JWT token against username.
     * 
     * @param token JWT token
     * @param username username to validate against
     * @return true if valid, false otherwise
     */
    public Boolean validateToken(String token, String username) {
        try {
            final String extractedUsername = extractUsername(token);
            boolean isValid = (extractedUsername.equals(username) && !isTokenExpired(token));
            
            if (isValid) {
                logger.debug("JWT token validated successfully for user: {}", username);
            } else {
                logger.warn("JWT token validation failed for user: {}", username);
            }
            
            return isValid;
        } catch (Exception e) {
            logger.error("JWT token validation error", e);
            return false;
        }
    }
}
