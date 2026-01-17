package com.medtrack.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String dosage;

    @ElementCollection
    @CollectionTable(name = "medicine_schedules", joinColumns = @JoinColumn(name = "medicine_id"))
    private List<InTakeSchedule> schedules;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private boolean active = true;
}