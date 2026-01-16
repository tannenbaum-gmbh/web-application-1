package com.demo.stockchecker.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for JwtAuthenticationFilter.
 * Tests filter behavior for various authentication scenarios.
 * 
 * @author Demo Team
 * @version 1.0.0
 */
@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private JwtAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        filter = new JwtAuthenticationFilter(jwtUtil);
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_WithValidToken_ShouldSetAuthentication() throws ServletException, IOException {
        // Arrange
        String token = "valid.jwt.token";
        String username = "testuser";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.extractUsername(token)).thenReturn(username);
        when(jwtUtil.validateToken(token, username)).thenReturn(true);

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(username, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithInvalidToken_ShouldNotSetAuthentication() throws ServletException, IOException {
        // Arrange
        String token = "invalid.jwt.token";
        String username = "testuser";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.extractUsername(token)).thenReturn(username);
        when(jwtUtil.validateToken(token, username)).thenReturn(false);

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithMissingAuthorizationHeader_ShouldNotSetAuthentication() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
        verify(jwtUtil, never()).extractUsername(anyString());
    }

    @Test
    void doFilterInternal_WithInvalidBearerFormat_ShouldNotSetAuthentication() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("InvalidFormat token");

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
        verify(jwtUtil, never()).extractUsername(anyString());
    }

    @Test
    void doFilterInternal_WithExpiredToken_ShouldNotSetAuthentication() throws ServletException, IOException {
        // Arrange
        String token = "expired.jwt.token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.extractUsername(token)).thenThrow(new RuntimeException("Token expired"));

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithMalformedToken_ShouldNotSetAuthentication() throws ServletException, IOException {
        // Arrange
        String token = "malformed.token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.extractUsername(token)).thenThrow(new RuntimeException("Malformed JWT"));

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WhenAuthenticationAlreadyExists_ShouldNotOverride() throws ServletException, IOException {
        // Arrange
        String token = "valid.jwt.token";
        String username = "testuser";
        
        // Set up existing authentication
        org.springframework.security.authentication.UsernamePasswordAuthenticationToken existingAuth =
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        "existinguser", null, java.util.Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(existingAuth);
        
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.extractUsername(token)).thenReturn(username);

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertEquals("existinguser", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        verify(jwtUtil, never()).validateToken(anyString(), anyString());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithEmptyToken_ShouldNotSetAuthentication() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer ");

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }
}
