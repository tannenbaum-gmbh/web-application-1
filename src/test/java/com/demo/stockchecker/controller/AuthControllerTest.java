package com.demo.stockchecker.controller;

import com.demo.stockchecker.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for AuthController.
 * Tests JWT token generation endpoint.
 * 
 * @author Demo Team
 * @version 1.0.0
 */
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void login_WhenValidUsername_ShouldReturnToken() throws Exception {
        // Arrange
        String username = "testuser";
        String token = "test.jwt.token";
        when(jwtUtil.generateToken(anyString())).thenReturn(token);

        AuthController.LoginRequest loginRequest = new AuthController.LoginRequest();
        loginRequest.setUsername(username);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token", is(token)))
                .andExpect(jsonPath("$.type", is("Bearer")))
                .andExpect(jsonPath("$.username", is(username)));
    }

    @Test
    void login_WhenUsernameIsBlank_ShouldReturn400() throws Exception {
        // Arrange
        AuthController.LoginRequest loginRequest = new AuthController.LoginRequest();
        loginRequest.setUsername("");

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_WhenUsernameIsNull_ShouldReturn400() throws Exception {
        // Arrange
        AuthController.LoginRequest loginRequest = new AuthController.LoginRequest();

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }
}
