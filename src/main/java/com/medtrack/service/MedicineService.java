package com.medtrack.service;

import com.medtrack.model.Medicine;
import com.medtrack.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MedicineService {

    @Autowired
    private MedicineRepository medicineRepository;

    public List<Medicine> findByUserId(Long userId) {
        return medicineRepository.findByUserId(userId);
    }

    public Medicine save(Medicine medicine) {
        return medicineRepository.save(medicine);
    }

    public List<Medicine> findAll() {
        return medicineRepository.findAll();
    }
}