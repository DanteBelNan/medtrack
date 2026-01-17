package com.medtrack.dto;

import lombok.Data;
import java.util.List;

@Data
public class MedicineDTO {
    private Long id;
    private String name;
    private String dosage;
    private boolean active;
    private List<InTakeScheduleDTO> schedules;
    private Long userId;
}