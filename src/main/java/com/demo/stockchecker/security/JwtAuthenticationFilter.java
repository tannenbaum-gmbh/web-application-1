package com.demo.stockchecker.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * JWT authentication filter that validates JWT tokens on each request.
 * Extends OncePerRequestFilter to ensure filter is executed once per request.
 * 
 * @author Demo Team
 * @version 1.0.0
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;

    /**
     * Constructor with dependency injection.
     * 
     * @param jwtUtil JWT utility for token operations
     */
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Filters incoming requests to validate JWT tokens.
     * 
     * @param request HTTP request
     * @param response HTTP response
     * @param filterChain filter chain
     * @throws ServletException if servlet error occurs
     * @throws IOException if I/O error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        String username = null;
        String jwt = null;

        // Extract JWT token from Authorization header
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            jwt = authorizationHeader.substring(BEARER_PREFIX.length());
            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                logger.error("Error extracting username from JWT: {}", e.getMessage());
            }
        }

        // Validate token and set authentication context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.validateToken(jwt, username)) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                logger.debug("Authentication set for user: {}", username);
            }
        }

        filterChain.doFilter(request, response);
    }
}
