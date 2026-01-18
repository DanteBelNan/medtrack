package com.medtrack.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Data
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String dosage;

    @ElementCollection
    @CollectionTable(name = "medicine_days", joinColumns = @JoinColumn(name = "medicine_id"))
    @Column(name = "day_of_week")
    private List<String> daysOfWeek;

    @ElementCollection
    @CollectionTable(name = "medicine_times", joinColumns = @JoinColumn(name = "medicine_id"))
    @Column(name = "intake_time")
    private List<LocalTime> intakeTimes;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("medicines")
    private User user;

    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active = true;
}