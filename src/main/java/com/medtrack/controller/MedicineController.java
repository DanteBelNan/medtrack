package com.medtrack.controller;

import com.medtrack.model.Medicine;
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
    public List<Medicine> list() {
        return medicineService.findAll();
    }

    @PostMapping
    public Medicine create(@RequestBody Medicine medicine) {
        return medicineService.save(medicine);
    }

    @GetMapping("/user/{userId}")
    public List<Medicine> byUserId(@PathVariable Long userId){
        return medicineService.findByUserId(userId);
    }
}