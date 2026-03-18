package com.demo.stockchecker.security;

import com.demo.stockchecker.config.SecurityConfig;
import com.demo.stockchecker.controller.StockController;
import com.demo.stockchecker.service.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for JWT Authentication.
 *
 * @author Demo Team
 * @version 1.0.0
 */
@WebMvcTest(StockController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, JwtUtil.class})
class JwtAuthenticationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @MockBean
    private StockService stockService;

    private String validToken;

    @BeforeEach
    void setUp() {
        validToken = jwtUtil.generateToken("testuser");
        when(stockService.getAllStocks()).thenReturn(new ArrayList<>());
    }

    @Test
    void getAllStocks_WithoutToken_ShouldReturn403() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/stocks"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllStocks_WithValidToken_ShouldReturn200() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/stocks")
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk());
    }

    @Test
    void getAllStocks_WithInvalidToken_ShouldReturn403() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/stocks")
                        .header("Authorization", "Bearer invalid.token.here"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllStocks_WithMalformedAuthorizationHeader_ShouldReturn403() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/stocks")
                        .header("Authorization", "InvalidPrefix " + validToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void health_WithoutToken_ShouldReturn200() throws Exception {
        // Health endpoint should be accessible without authentication
        // Act & Assert
        mockMvc.perform(get("/api/stocks/health"))
                .andExpect(status().isOk());
    }
}
