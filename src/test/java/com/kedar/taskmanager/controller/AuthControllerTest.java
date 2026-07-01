package com.kedar.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kedar.taskmanager.dto.AuthResponse;
import com.kedar.taskmanager.dto.LoginRequest;
import com.kedar.taskmanager.dto.RegisterRequest;
import com.kedar.taskmanager.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void register_ShouldReturn200_WhenRequestIsValid() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setName("Kedar");
        request.setEmail("kedar@example.com");
        request.setPassword("password123");

        AuthResponse response = new AuthResponse("mock-jwt-token", "Kedar", "USER");

        when(authService.register(any(RegisterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"))
                .andExpect(jsonPath("$.name").value("Kedar"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void login_ShouldReturn200_WhenLoginSucceeds() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("kedar@example.com");
        request.setPassword("password123");

        AuthResponse response = new AuthResponse("mock-jwt-token", "Kedar", "USER");

        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"))
                .andExpect(jsonPath("$.name").value("Kedar"))
                .andExpect(jsonPath("$.role").value("USER"));
    }
}
