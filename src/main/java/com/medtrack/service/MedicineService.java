package com.medtrack.service;

import com.medtrack.model.Medicine;
import com.medtrack.model.User;
import com.medtrack.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.medtrack.dto.MedicineDTO;
import com.medtrack.mapper.MedicineMapper;
import com.medtrack.repository.UserRepository;

@Service
public class MedicineService {

    private final MedicineRepository medicineRepository;
    private final UserRepository userRepository;
    private final MedicineMapper medicineMapper;

    public MedicineService(MedicineRepository medicineRepository,
                           UserRepository userRepository,
                           MedicineMapper medicineMapper) {
        this.medicineRepository = medicineRepository;
        this.userRepository = userRepository;
        this.medicineMapper = medicineMapper;
    }

    public List<MedicineDTO> findAll() {
        return medicineRepository.findAll().stream()
                .map(medicineMapper::toDTO)
                .collect(Collectors.toList());
    }

    public MedicineDTO findById(Long id) {
        return medicineRepository.findById(id)
                .map(medicineMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));
    }

    public MedicineDTO save(MedicineDTO medicineDTO) {
        Medicine medicine = medicineMapper.toEntity(medicineDTO);

        if (medicineDTO.getUserId() != null) {
            User user = userRepository.findById(medicineDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            medicine.setUser(user);
        }

        Medicine savedMedicine = medicineRepository.save(medicine);
        return medicineMapper.toDTO(savedMedicine);
    }

    public List<MedicineDTO> findByUserId(Long userId) {
        return medicineRepository.findByUserId(userId).stream()
                .map(medicineMapper::toDTO)
                .collect(Collectors.toList());
    }
}