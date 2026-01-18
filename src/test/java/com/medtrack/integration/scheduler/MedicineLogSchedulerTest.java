package com.medtrack.integration.scheduler;

import com.medtrack.IntegrationTestBase;
import com.medtrack.model.Medicine;
import com.medtrack.model.User;
import com.medtrack.model.MedicineLog;
import com.medtrack.repository.MedicineLogRepository;
import com.medtrack.repository.MedicineRepository;
import com.medtrack.repository.UserRepository;
import com.medtrack.scheduler.MedicineLogScheduler;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MedicineLogSchedulerTest extends IntegrationTestBase {

    @Autowired
    private MedicineLogScheduler scheduler;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private MedicineLogRepository logRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        logRepository.deleteAllInBatch();
        medicineRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    void shouldGenerateLogsForActiveMedicines() {
        // Arrange
        User user = new User();
        user.setName("John Doe");
        user.setEmail("johndoe" + System.currentTimeMillis() + "@email.com");
        user.setPassword("encoded_pass");
        user = userRepository.save(user);

        Medicine med = new Medicine();
        med.setName("Ibuprofeno");
        med.setActive(true);
        med.setUser(user);

        String today = LocalDate.now().getDayOfWeek().name();
        med.setDaysOfWeek(List.of(today));
        med.setIntakeTimes(List.of(LocalTime.of(10, 0), LocalTime.of(22, 0)));

        medicineRepository.save(med);

        scheduler.generateDailyLogs();

        List<MedicineLog> logs = logRepository.findAll();
        assertThat(logs).hasSize(2);
        assertThat(logs.get(0).getMedicine().getName()).isEqualTo("Ibuprofeno");
    }
}