package com.medtrack.scheduler;

import com.medtrack.model.Medicine;
import com.medtrack.repository.MedicineRepository;
import com.medtrack.service.MedicineLogService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;

@Component
public class MedicineLogScheduler {

    private final MedicineRepository medicineRepository;
    private final MedicineLogService medicineLogService;

    public MedicineLogScheduler(MedicineRepository medicineRepository, MedicineLogService medicineLogService) {
        this.medicineRepository = medicineRepository;
        this.medicineLogService = medicineLogService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void generateDailyLogs() {
        List<Medicine> activeMedicines = medicineRepository.findAllByActiveTrue();
        LocalDate today = LocalDate.now();

        for (Medicine medicine : activeMedicines) {
            medicineLogService.createLogsForMedicine(medicine, today);
        }
    }
}