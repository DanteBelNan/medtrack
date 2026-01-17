package com.medtrack.mapper;

import com.medtrack.dto.InTakeScheduleDTO;
import com.medtrack.dto.MedicineDTO;
import com.medtrack.model.InTakeSchedule;
import com.medtrack.model.Medicine;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class MedicineMapper {

    public MedicineDTO toDTO(Medicine medicine) {
        if (medicine == null) return null;

        MedicineDTO dto = new MedicineDTO();
        dto.setId(medicine.getId());
        dto.setName(medicine.getName());
        dto.setDosage(medicine.getDosage());
        dto.setActive(medicine.isActive());

        if (medicine.getUser() != null) {
            dto.setUserId(medicine.getUser().getId());
        }

        if (medicine.getSchedules() != null) {
            dto.setSchedules(medicine.getSchedules().stream()
                    .map(this::toScheduleDTO)
                    .collect(Collectors.toList()));
        } else {
            dto.setSchedules(new ArrayList<>());
        }

        return dto;
    }

    private InTakeScheduleDTO toScheduleDTO(InTakeSchedule schedule) {
        if (schedule == null) return null;

        InTakeScheduleDTO dto = new InTakeScheduleDTO();
        dto.setDayOfWeek(schedule.getDayOfWeek());
        dto.setIntakeTime(schedule.getIntakeTime());
        return dto;
    }

    public Medicine toEntity(MedicineDTO dto) {
        if (dto == null) return null;

        Medicine medicine = new Medicine();
        medicine.setName(dto.getName());
        medicine.setDosage(dto.getDosage());
        medicine.setActive(dto.isActive());

        if (dto.getSchedules() != null) {
            medicine.setSchedules(dto.getSchedules().stream()
                    .map(this::toScheduleEntity)
                    .collect(Collectors.toList()));
        }

        return medicine;
    }

    private InTakeSchedule toScheduleEntity(InTakeScheduleDTO dto) {
        if (dto == null) return null;
        InTakeSchedule schedule = new InTakeSchedule();
        schedule.setDayOfWeek(dto.getDayOfWeek());
        schedule.setIntakeTime(dto.getIntakeTime());
        return schedule;
    }
}