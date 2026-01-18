package com.medtrack.service;

import com.medtrack.model.Medicine;
import com.medtrack.model.MedicineLog;
import com.medtrack.repository.MedicineLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class MedicineLogServiceTest {

    @Mock
    private MedicineLogRepository logRepository;

    @InjectMocks
    private MedicineLogService logService;

    @Test
    void shouldCreateLogsOnlyOnCorrectDays() {
        Medicine med = new Medicine();
        med.setName("Ibuprofeno");
        med.setDaysOfWeek(List.of("MONDAY", "WEDNESDAY"));
        med.setIntakeTimes(List.of(LocalTime.of(10, 0), LocalTime.of(18, 0)));

        LocalDate monday = LocalDate.of(2026, 1, 19);

        logService.createLogsForMedicine(med, monday);

        verify(logRepository, times(2)).save(any(MedicineLog.class));
    }

    @Test
    void shouldNotCreateLogsOnIncorrectDays() {
        // Arrange
        Medicine med = new Medicine();
        med.setDaysOfWeek(List.of("MONDAY"));
        med.setIntakeTimes(List.of(LocalTime.of(10, 0)));

        LocalDate tuesday = LocalDate.of(2026, 1, 20);

        logService.createLogsForMedicine(med, tuesday);

        verify(logRepository, never()).save(any(MedicineLog.class));
    }

    @Test
    void shouldCreateLogsWithCorrectScheduledTime() {
        Medicine med = new Medicine();
        med.setDaysOfWeek(List.of("MONDAY"));
        med.setIntakeTimes(List.of(LocalTime.of(10, 0)));
        LocalDate monday = LocalDate.of(2026, 1, 19);

        logService.createLogsForMedicine(med, monday);

        verify(logRepository).save(argThat(log ->
                log.getScheduledTime().equals(LocalDateTime.of(monday, LocalTime.of(10, 0)))
        ));
    }
}