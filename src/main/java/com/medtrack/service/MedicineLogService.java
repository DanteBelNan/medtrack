package com.medtrack.service;

import com.medtrack.model.Medicine;
import com.medtrack.model.MedicineLog;
import com.medtrack.model.LogStatus;
import com.medtrack.repository.MedicineLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class MedicineLogService {

    private final MedicineLogRepository medicineLogRepository;

    public MedicineLogService(MedicineLogRepository medicineLogRepository) {
        this.medicineLogRepository = medicineLogRepository;
    }

    @Transactional
    public void createLogsForMedicine(Medicine medicine, LocalDate date) {
        String dayOfWeekName = date.getDayOfWeek().name();

        if (medicine.getDaysOfWeek().contains(dayOfWeekName)) {
            for (LocalTime intakeTime : medicine.getIntakeTimes()) {
                MedicineLog log = new MedicineLog();
                log.setMedicine(medicine);
                log.setUser(medicine.getUser());
                log.setScheduledTime(LocalDateTime.of(date, intakeTime));
                log.setStatus(LogStatus.PENDING);
                medicineLogRepository.save(log);
            }
        }
    }
}