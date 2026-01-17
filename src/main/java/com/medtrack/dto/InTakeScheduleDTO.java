package com.medtrack.dto;

import lombok.Data;
import java.time.LocalTime;

@Data
public class InTakeScheduleDTO {
    private String dayOfWeek;
    private LocalTime intakeTime;
}