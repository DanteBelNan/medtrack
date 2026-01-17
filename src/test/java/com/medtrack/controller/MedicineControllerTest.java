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
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;

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
        medicineRepository.deleteAll();
        userRepository.deleteAll();
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
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Ibuprofeno"))
                .andExpect(jsonPath("$[1].name").value("Aspirina"));
    }

    @Test
    void shouldCreateMedicineWithUser() throws Exception {
        User user = new User();
        user.setName("Dante");
        user.setEmail("dante@utn.com");
        user = userRepository.save(user);

        String medicineJson = """
    {
      "name": "Ibuprofeno",
      "dosage": "600mg",
      "userId": %d
    }
    """.formatted(user.getId());

        mockMvc.perform(post("/api/medicines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(medicineJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ibuprofeno"))
                .andExpect(jsonPath("$.userId").value(user.getId().intValue()));
    }

    @Test
    void shouldReturnMedicinesByUserId() throws Exception {
        // 1. Setup User
        User user = new User();
        user.setName("John Doe");
        user.setEmail("johndoe@email.com");
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
                .andExpect(jsonPath("$[0].userId").value(user.getId().intValue()));
    }

    @Test
    void shouldReturnMedicineById() throws Exception {
        Medicine med = new Medicine();
        med.setName("Enalapril");
        med.setDosage("5mg");
        med = medicineRepository.save(med);

        mockMvc.perform(get("/api/medicines/" + med.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Enalapril"))
                .andExpect(jsonPath("$.dosage").value("5mg"));
    }
}