package com.medtrack.integration.controller;

import com.medtrack.IntegrationTestBase;
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
class MedicineControllerTest extends IntegrationTestBase {

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

    private String userToken;
    private String adminToken;
    private User testUser;
    private User adminUser;

    @BeforeEach
    void setup() throws Exception {
        medicineRepository.deleteAll();
        userRepository.deleteAll();

        UserRegistrationDTO reg = new UserRegistrationDTO();
        reg.setName("John Doe");
        reg.setEmail("johndoe@email.com");
        reg.setPassword("password123");
        userService.register(reg);
        testUser = userRepository.findByEmail("johndoe@email.com").orElseThrow();
        userToken = loginAndGetToken("johndoe@email.com", "password123");

        User admin = new User();
        admin.setName("Admin User");
        admin.setEmail("admin@medtrack.com");
        admin.setPassword(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("admin123"));
        admin.setRole(com.medtrack.model.Role.ADMIN);
        adminUser = userRepository.save(admin);
        adminToken = loginAndGetToken("admin@medtrack.com", "admin123");
    }

    private String loginAndGetToken(String email, String password) throws Exception {
        LoginRequestDTO login = new LoginRequestDTO();
        login.setEmail(email);
        login.setPassword(password);

        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(response).get("token").asText();
    }

    @Test
    void adminShouldAccessAllMedicines() throws Exception {
        mockMvc.perform(get("/api/medicines/all")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

    @Test
    void adminShouldAccessSpecificUserMedicines() throws Exception {
        mockMvc.perform(get("/api/medicines/user/" + testUser.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

    @Test
    void userShouldBeForbiddenFromAdminEndpoints() throws Exception {
        mockMvc.perform(get("/api/medicines/all")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Access Denied"));

        mockMvc.perform(get("/api/medicines/user/" + adminUser.getId())
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldCreateMedicineWithSchedules() throws Exception {
        String medicineJson = """
    {
      "name": "Ibuprofeno",
      "dosage": "600mg",
      "daysOfWeek": ["MONDAY", "WEDNESDAY", "FRIDAY"],
      "intakeTimes": ["08:00:00", "20:00:00"]
    }
    """;

        mockMvc.perform(post("/api/medicines")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(medicineJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ibuprofeno"))
                .andExpect(jsonPath("$.daysOfWeek", hasSize(3)))
                .andExpect(jsonPath("$.daysOfWeek[0]").value("MONDAY"))
                .andExpect(jsonPath("$.intakeTimes", hasSize(2)))
                .andExpect(jsonPath("$.intakeTimes[0]").value("08:00:00"));
    }
    @Test
    void shouldReturnMyMedicines() throws Exception {
        Medicine m1 = new Medicine();
        m1.setName("Ibuprofeno");
        m1.setUser(testUser);

        medicineRepository.save(m1);

        mockMvc.perform(get("/api/medicines/my")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Ibuprofeno"));
    }

    @Test
    void shouldReturnMedicineById() throws Exception {
        Medicine med = new Medicine();
        med.setName("Enalapril");
        med.setDosage("5mg");
        med.setUser(testUser);
        med = medicineRepository.save(med);

        mockMvc.perform(get("/api/medicines/" + med.getId())
                        .header("Authorization", "Bearer " + userToken))
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