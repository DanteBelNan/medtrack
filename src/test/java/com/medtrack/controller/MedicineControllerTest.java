package com.medtrack.controller;

import com.medtrack.dto.LoginRequestDTO;
import com.medtrack.dto.UserRegistrationDTO;
import com.medtrack.model.Medicine;
import com.medtrack.model.User;
import com.medtrack.repository.MedicineRepository;
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

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;
    private User testUser;

    @BeforeEach
    void setup() throws Exception {
        // Limpiar base de datos
        medicineRepository.deleteAll();
        userRepository.deleteAll();

        UserRegistrationDTO reg = new UserRegistrationDTO();
        reg.setName("John Doe");
        reg.setEmail("johndoe@email.com");
        reg.setPassword("password123");
        userService.register(reg);

        testUser = userRepository.findByEmail("johndoe@email.com").orElseThrow();

        LoginRequestDTO login = new LoginRequestDTO();
        login.setEmail("johndoe@email.com");
        login.setPassword("password123");

        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        this.token = objectMapper.readTree(response).get("token").asText();
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

        mockMvc.perform(get("/api/medicines")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Ibuprofeno"))
                .andExpect(jsonPath("$[1].name").value("Aspirina"));
    }

    @Test
    void shouldCreateMedicineWithUser() throws Exception {
        String medicineJson = """
        {
          "name": "Ibuprofeno",
          "dosage": "600mg",
          "userId": %d
        }
        """.formatted(testUser.getId());

        mockMvc.perform(post("/api/medicines")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(medicineJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ibuprofeno"))
                .andExpect(jsonPath("$.userId").value(testUser.getId().intValue()));
    }

    @Test
    void shouldReturnMedicinesByUserId() throws Exception {
        Medicine med = new Medicine();
        med.setName("Loratadina");
        med.setDosage("10mg");
        med.setUser(testUser);
        medicineRepository.save(med);

        mockMvc.perform(get("/api/medicines/user/" + testUser.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Loratadina"))
                .andExpect(jsonPath("$[0].userId").value(testUser.getId().intValue()));
    }

    @Test
    void shouldReturnMedicineById() throws Exception {
        Medicine med = new Medicine();
        med.setName("Enalapril");
        med.setDosage("5mg");
        med = medicineRepository.save(med);

        mockMvc.perform(get("/api/medicines/" + med.getId())
                        .header("Authorization", "Bearer " + token)) // Header JWT
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Enalapril"))
                .andExpect(jsonPath("$.dosage").value("5mg"));
    }

    @Test
    void shouldReturn401WhenNoTokenProvided() throws Exception {
        mockMvc.perform(get("/api/medicines"))
                .andExpect(status().isForbidden());
    }
}