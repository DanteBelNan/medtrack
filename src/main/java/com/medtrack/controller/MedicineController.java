package com.medtrack.controller;

import com.medtrack.dto.MedicineDTO;
import com.medtrack.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<MedicineDTO> listAll() {
        return medicineService.findAll();
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<MedicineDTO> byUserId(@PathVariable Long userId) {
        return medicineService.findByUserId(userId);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public MedicineDTO create(@RequestBody MedicineDTO medicineDTO) {
        return medicineService.save(medicineDTO);
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<MedicineDTO> getMyMedicines() {
        return medicineService.findMyMedicines();
    }

    @GetMapping("/{id}")
    public MedicineDTO getById(@PathVariable Long id) {
        return medicineService.findById(id);
    }
}