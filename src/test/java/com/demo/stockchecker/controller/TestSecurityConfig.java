package com.demo.stockchecker.controller;

import com.demo.stockchecker.security.JwtUtil;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Test security configuration that disables security for controller unit tests.
 * This allows existing controller tests to pass without needing JWT tokens.
 * Mocks JwtUtil so the real JwtAuthenticationFilter can be constructed
 * but won't authenticate (mock returns false by default).
 * 
 * @author Demo Team
 * @version 1.0.0
 */
@TestConfiguration
public class TestSecurityConfig {

    @MockBean
    private JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );
        return http.build();
    }
}
