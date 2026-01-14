package com.demo.stockchecker.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Test security configuration that disables security for controller tests.
 * 
 * @author Demo Team
 * @version 1.0.0
 */
@TestConfiguration
@EnableWebSecurity
public class TestSecurityConfig {

    /**
     * Configures security to permit all requests for testing.
     * 
     * @param http HTTP security configuration
     * @return configured security filter chain
     * @throws Exception if configuration error occurs
     */
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
