package com.medtrack.model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import java.time.LocalTime;

@Embeddable
@Data
public class InTakeSchedule {
    private String dayOfWeek;
    private LocalTime intakeTime;
}