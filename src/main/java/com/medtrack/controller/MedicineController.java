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

    @GetMapping
    public List<MedicineDTO> list() {
        return medicineService.findAll();
    }

    @PostMapping
    public MedicineDTO create(@RequestBody MedicineDTO medicineDTO) {
        return medicineService.save(medicineDTO);
    }

    @GetMapping("/user/{userId}")
    public List<MedicineDTO> byUserId(@PathVariable Long userId){
        return medicineService.findByUserId(userId);
    }

    @GetMapping("/{id}")
    public MedicineDTO getById(@PathVariable Long id) {
        return medicineService.findById(id);
    }
}