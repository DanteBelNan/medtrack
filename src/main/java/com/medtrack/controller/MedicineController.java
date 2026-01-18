package com.medtrack.controller;

import com.medtrack.dto.MedicineDTO;
import com.medtrack.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    @GetMapping("/all")
    public List<MedicineDTO> listAll() { //TODO: This endpoint should be only allowed for specific roles
        return medicineService.findAll();
    }

    @GetMapping("/user/{userId}")
    public List<MedicineDTO> byUserId(@PathVariable Long userId) { // TODO: Another endpoint for admin role
        return medicineService.findByUserId(userId);
    }

    @PostMapping
    public MedicineDTO create(@RequestBody MedicineDTO medicineDTO) {
        return medicineService.save(medicineDTO);
    }

    @GetMapping("/my")
    public List<MedicineDTO> getMyMedicines() {
        return medicineService.findMyMedicines();
    }

    @GetMapping("/{id}")
    public MedicineDTO getById(@PathVariable Long id) {
        return medicineService.findById(id);
    }
}