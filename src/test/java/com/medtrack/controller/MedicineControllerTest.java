package com.medtrack.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class MedicineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldListMedicine() throws Exception {
        mockMvc.perform(get("/api/medicines"))
                .andExpect(status().isOk());
    }

    @Test
     void shouldCreateMedicine() throws Exception {
        String medicineJson = "{\"name\":\"Ibuprofeno\",\"dosage\":\"600mg\",\"active\":true}";

        mockMvc.perform(post("/api/medicines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(medicineJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ibuprofeno"));
    }
}