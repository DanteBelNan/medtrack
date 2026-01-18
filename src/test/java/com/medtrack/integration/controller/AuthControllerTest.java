package com.medtrack.integration.controller;

import com.medtrack.IntegrationTestBase;
import com.medtrack.dto.LoginRequestDTO;
import com.medtrack.dto.UserRegistrationDTO;
import com.medtrack.repository.UserRepository;
import com.medtrack.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AuthControllerTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    void shouldLoginAndReturnToken() throws Exception {
        UserRegistrationDTO reg = new UserRegistrationDTO();
        reg.setName("John Doe");
        reg.setEmail("johndoe@email.com");
        reg.setPassword("password123");
        userService.register(reg);

        LoginRequestDTO login = new LoginRequestDTO();
        login.setEmail("johndoe@email.com");
        login.setPassword("password123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").isString());
    }

    @Test
    void shouldFailLoginWithWrongPassword() throws Exception {
        UserRegistrationDTO reg = new UserRegistrationDTO();
        reg.setName("John Doe");
        reg.setEmail("johndoe@email.com");
        reg.setPassword("password123");
        userService.register(reg);

        LoginRequestDTO login = new LoginRequestDTO();
        login.setEmail("johndoe@email.com");
        login.setPassword("wrong_pass");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized());
    }
}