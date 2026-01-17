package com.medtrack.controller;

import com.medtrack.model.Medicine;
import com.medtrack.model.User;
import com.medtrack.repository.MedicineRepository;
import com.medtrack.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class MedicineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        medicineRepository.deleteAll();
    }

    @Test
    void shouldReturnAllMedicines() throws Exception {
        Medicine m1 = new Medicine();
        m1.setName("Ibuprofeno");
        m1.setDosage("600mg");

        Medicine m2 = new Medicine();
        m2.setName("Aspirina");
        m2.setDosage("100mg");

        medicineRepository.save(m1);
        medicineRepository.save(m2);

        mockMvc.perform(get("/api/medicines"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))) // Verificamos que vengan 2
                .andExpect(jsonPath("$[0].name").value("Ibuprofeno"))
                .andExpect(jsonPath("$[1].name").value("Aspirina"));
    }

    @Test
    void shouldCreateMedicine() throws Exception {
        String medicineJson = """
        {
          "name": "Ibuprofeno",
          "dosage": "600mg",
          "active": true,
          "schedules": [
            {"dayOfWeek": "Monday", "intakeTime": "08:00:00"},
            {"dayOfWeek": "Wednesday", "intakeTime": "20:00:00"}
          ]
        }
        """;
        mockMvc.perform(post("/api/medicines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(medicineJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Ibuprofeno"))
                .andExpect(jsonPath("$.schedules", hasSize(2)))
                .andExpect(jsonPath("$.schedules[0].dayOfWeek").value("Monday"));
    }

    @Test
    void shouldReturnMedicinesByUserId() throws Exception {
        // --- SETUP ---
        // 1. Creamos un usuario
        User user = new User();
        user.setName("Dante Beltrán");
        user.setEmail("dante.beltran@utn.edu.ar");
        user = userRepository.save(user);

        Medicine med = new Medicine();
        med.setName("Loratadina");
        med.setDosage("10mg");
        med.setUser(user);
        medicineRepository.save(med);

        mockMvc.perform(get("/api/medicines/user/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Loratadina"))
                .andExpect(jsonPath("$[0].user.id").value(user.getId().intValue()));
    }
}