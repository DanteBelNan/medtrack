package com.medtrack.dto;

import com.medtrack.model.LogStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MedicineLogDTO {
    private Long id;
    private Long medicineId;
    private String medicineName;
    private String dosage;
    private Long userId;
    private LocalDateTime scheduledTime;
    private LocalDateTime takenAt;
    private LogStatus status;
}