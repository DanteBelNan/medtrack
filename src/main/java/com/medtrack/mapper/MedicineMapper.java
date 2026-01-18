package com.medtrack.mapper;

import com.medtrack.dto.MedicineDTO;
import com.medtrack.model.Medicine;
import org.springframework.stereotype.Component;
import java.util.ArrayList;

@Component
public class MedicineMapper {

    public MedicineDTO toDTO(Medicine medicine) {
        if (medicine == null) return null;

        MedicineDTO dto = new MedicineDTO();
        dto.setId(medicine.getId());
        dto.setName(medicine.getName());
        dto.setDosage(medicine.getDosage());
        dto.setActive(medicine.isActive());

        dto.setDaysOfWeek(medicine.getDaysOfWeek() != null ? new ArrayList<>(medicine.getDaysOfWeek()) : new ArrayList<>());
        dto.setIntakeTimes(medicine.getIntakeTimes() != null ? new ArrayList<>(medicine.getIntakeTimes()) : new ArrayList<>());

        if (medicine.getUser() != null) {
            dto.setUserId(medicine.getUser().getId());
        }

        return dto;
    }

    public Medicine toEntity(MedicineDTO dto) {
        if (dto == null) return null;

        Medicine medicine = new Medicine();
        medicine.setName(dto.getName());
        medicine.setDosage(dto.getDosage());
        medicine.setActive(dto.isActive());

        medicine.setDaysOfWeek(dto.getDaysOfWeek());
        medicine.setIntakeTimes(dto.getIntakeTimes());

        return medicine;
    }
}