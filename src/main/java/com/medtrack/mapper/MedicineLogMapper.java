package com.medtrack.mapper;

import com.medtrack.dto.MedicineLogDTO;
import com.medtrack.model.MedicineLog;
import org.springframework.stereotype.Component;

@Component
public class MedicineLogMapper {
    public MedicineLogDTO toDTO(MedicineLog log) {
        if (log == null) return null;
        MedicineLogDTO dto = new MedicineLogDTO();
        dto.setId(log.getId());
        dto.setMedicineId(log.getMedicine().getId());
        dto.setMedicineName(log.getMedicine().getName());
        dto.setDosage(log.getMedicine().getDosage());
        dto.setUserId(log.getUser().getId());
        dto.setScheduledTime(log.getScheduledTime());
        dto.setTakenAt(log.getTakenAt());
        dto.setStatus(log.getStatus());
        return dto;
    }
}