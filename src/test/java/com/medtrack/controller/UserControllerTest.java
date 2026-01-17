package com.medtrack.controller;

import com.medtrack.dto.UserRegistrationDTO;
import com.medtrack.repository.UserRepository;
import com.medtrack.repository.MedicineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false) // Desactivamos seguridad para este test inicial
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        medicineRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldRegisterUserCorrectly() throws Exception {
        UserRegistrationDTO registration = new UserRegistrationDTO();
        registration.setName("Dante Beltran");
        registration.setEmail("dante.beltran@utn.edu.ar");
        registration.setPassword("securePassword123");

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registration)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Dante Beltran"))
                .andExpect(jsonPath("$.email").value("dante.beltran@utn.edu.ar"))
                .andExpect(jsonPath("$.password").doesNotExist()); // ¡Vital por seguridad!
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        // Registramos dos usuarios directamente para probar el listado
        UserRegistrationDTO u1 = new UserRegistrationDTO();
        u1.setName("User One");
        u1.setEmail("one@test.com");
        u1.setPassword("pass1");

        UserRegistrationDTO u2 = new UserRegistrationDTO();
        u2.setName("User Two");
        u2.setEmail("two@test.com");
        u2.setPassword("pass2");

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(u1)));

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(u2)));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("User One"))
                .andExpect(jsonPath("$[1].name").value("User Two"));
    }
}