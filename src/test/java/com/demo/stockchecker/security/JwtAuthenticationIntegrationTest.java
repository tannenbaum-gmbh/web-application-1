package com.demo.stockchecker.security;

import com.demo.stockchecker.config.SecurityConfig;
import com.demo.stockchecker.service.StockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for JWT authentication flow.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
class JwtAuthenticationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @MockBean
    private StockService stockService;

    @Test
    void testAccessProtectedEndpoint_WithoutToken_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/api/stocks"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testAccessProtectedEndpoint_WithInvalidToken_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/api/stocks")
                        .header("Authorization", "Bearer invalid.token.here"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testAccessProtectedEndpoint_WithValidToken_ShouldReturn200() throws Exception {
        String token = jwtUtil.generateToken("testuser");

        mockMvc.perform(get("/api/stocks")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void testAccessHealthEndpoint_WithoutToken_ShouldReturn200() throws Exception {
        mockMvc.perform(get("/api/stocks/health"))
                .andExpect(status().isOk());
    }
}
