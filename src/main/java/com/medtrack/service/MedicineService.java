package com.medtrack.service;

import com.medtrack.dto.MedicineDTO;
import com.medtrack.mapper.MedicineMapper;
import com.medtrack.model.Medicine;
import com.medtrack.model.User;
import com.medtrack.repository.MedicineRepository;
import com.medtrack.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    private User getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

    public List<MedicineDTO> findAll() {
        return medicineRepository.findAll().stream()
                .map(medicineMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<MedicineDTO> findByUserId(Long userId) {
        return medicineRepository.findByUserId(userId).stream()
                .map(medicineMapper::toDTO)
                .collect(Collectors.toList());
    }

    public MedicineDTO findById(Long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));

        User currentUser = getAuthenticatedUser();
        if (!medicine.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Unauthorized access to this medicine");
        }

        return medicineMapper.toDTO(medicine);
    }

    public MedicineDTO save(MedicineDTO medicineDTO) {
        Medicine medicine = medicineMapper.toEntity(medicineDTO);

        medicine.setUser(getAuthenticatedUser());

        Medicine savedMedicine = medicineRepository.save(medicine);
        return medicineMapper.toDTO(savedMedicine);
    }

    public List<MedicineDTO> findMyMedicines() {
        User user = getAuthenticatedUser();
        return medicineRepository.findByUserId(user.getId()).stream()
                .map(medicineMapper::toDTO)
                .collect(Collectors.toList());
    }
}