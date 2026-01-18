package com.medtrack.dto;

import lombok.Data;
import java.time.LocalTime;
import java.util.List;

@Data
public class MedicineDTO {
    private Long id;
    private String name;
    private String dosage;
    private boolean active;
    private List<String> daysOfWeek;
    private List<LocalTime> intakeTimes;
    private Long userId;
}