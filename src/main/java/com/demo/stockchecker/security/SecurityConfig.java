package com.demo.stockchecker.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration for the application.
 * Configures JWT-based authentication and authorization.
 * 
 * @author Demo Team
 * @version 1.0.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures security filter chain.
     * 
     * @param http HTTP security configuration
     * @param jwtAuthenticationFilter JWT authentication filter
     * @param jwtAuthenticationEntryPoint custom entry point for authentication errors
     * @return configured security filter chain
     * @throws Exception if configuration error occurs
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, 
                                                  JwtAuthenticationFilter jwtAuthenticationFilter,
                                                  JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) throws Exception {
        http
            // CSRF protection is disabled for JWT-based authentication
            // JWT tokens are stateless and typically sent in Authorization headers (not cookies)
            // CSRF attacks primarily target cookie-based authentication
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/login").permitAll()
                .requestMatchers("/api/stocks/health").permitAll()
                .anyRequest().authenticated()
            )
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Creates JWT authentication filter bean.
     * 
     * @param jwtUtil JWT utility
     * @return JWT authentication filter
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtUtil jwtUtil) {
        return new JwtAuthenticationFilter(jwtUtil);
    }
}
